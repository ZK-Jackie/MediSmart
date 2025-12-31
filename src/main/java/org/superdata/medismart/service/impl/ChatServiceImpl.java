package org.superdata.medismart.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.superdata.medismart.common.exception.ServiceException;
import org.superdata.medismart.entity.ChatConversation;
import org.superdata.medismart.entity.ChatMessage;
import org.superdata.medismart.entity.request.UserChatRequest;
import org.superdata.medismart.mapper.sql.ChatConversationMapper;
import org.superdata.medismart.service.ChatService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Value("${chat.stream.url}")
    private String chatUrl;

    @Value("${chat.history.url}")
    private String chatHistoryUrl;

    @Resource
    private EventSource.Factory eventSourceFactory;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private ChatConversationMapper chatConversationMapper;

    @Override
    public SseEmitter connect(String sessionId) {
        return SseEmitterHolder.getConnect(sessionId);
    }

    @Override
    public SseEmitter connect(String userId, String convId) {
        return SseEmitterHolder.getConnect(getSessionId(userId, convId));
    }

    @Override
    public Map<String, Object> stream(UserChatRequest userChatRequest) {
        // 1. 检查参数
        if (userChatRequest == null || userChatRequest.getUserId() == null) {
            throw new ServiceException("输入不能为空");
        }

        // 2. 判断是否为新会话
        String conversationId = userChatRequest.getConversationId();
        boolean isNewConversation = (conversationId == null || conversationId.isEmpty());
        
        if (isNewConversation) {
            // 生成新的conversationId
            conversationId = UUID.randomUUID().toString();
            userChatRequest.setConversationId(conversationId);
            
            // 创建新会话记录
            ChatConversation conversation = new ChatConversation();
            conversation.setConversationId(conversationId);
            conversation.setUserId(Long.parseLong(userChatRequest.getUserId()));
            conversation.setConversationTitle("新对话");
            conversation.setDelFlag(0);
            chatConversationMapper.insert(conversation);
        }

        // 3. 检查是否已经连接
        if (SseEmitterHolder.existsConnect(
                getSessionId(userChatRequest.getUserId(), conversationId))
        ) {
            throw new ServiceException("已经连接");
        }

        // 4. 创建 SseEmitter
        SseEmitter emitter = new SseEmitter(180000L); // 3分钟超时
        String sessionId = UUID.randomUUID().toString();

        // 5. 创建请求
        RequestBody body = RequestBody.create(
                JSONUtil.toJsonStr(userChatRequest),
                okhttp3.MediaType.parse("application/json; charset=utf-8")
        );
        Request request = new Request.Builder()
                .url(chatUrl)
                .post(body)
                .header("Accept", "text/event-stream")
                .header("Cache-Control", "no-cache")
                .header("Connection", "keep-alive")
                .build();

        // 6. 注册事件及监听器
        EventSourceListener eventSourceListener = new EventSourceListener() {
            @Override
            public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
                log.info("[会话{}] 连接成功", sessionId);
            }

            @Override
            public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String data) {
                try {
                    // 转发数据到前端
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .id(sessionId)
                            .name("chat")
                            .data(data);

                    // 使用synchronized确保线程安全
                    synchronized (emitter) {
                        emitter.send(event);
                    }
                } catch (IOException e) {
                    emitter.completeWithError(e);
                    SseEmitterHolder.removeConnect(sessionId);
                }
            }

            @Override
            public void onFailure(@NotNull EventSource eventSource, Throwable t, Response response) {
                // 处理异常情况
                String errorMsg = (t != null) ? t.getMessage() : "未知错误";
                if (response != null && !response.isSuccessful()) {
                    errorMsg = "HTTP错误: " + response.code();
                }
                log.info("[会话{}] 连接失败: {}", sessionId, errorMsg);
                emitter.completeWithError(t != null ? t : new ServiceException(errorMsg));
                SseEmitterHolder.removeConnect(sessionId);
            }

            @Override
            public void onClosed(@NotNull EventSource eventSource) {
                log.info("[会话{}] 连接关闭", sessionId);
                emitter.complete();
                SseEmitterHolder.removeConnect(sessionId);
            }
        };
        EventSource eventSource = eventSourceFactory.newEventSource(request, eventSourceListener);

        // 包裹为 SseEmitter
        emitter.onCompletion(() -> {
            log.info("[会话{}] 完成", sessionId);
            eventSource.cancel();
        });
        emitter.onTimeout(eventSource::cancel);
        emitter.onError(throwable -> eventSource.cancel());

        // 将 SseEmitter 存储到 SseEmitterHolder 中
        SseEmitterHolder.createConnect(sessionId, emitter);
        
        // 返回sessionId和conversationId
        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", sessionId);
        result.put("conversationId", conversationId);
        return result;
    }

    @Override
    public List<ChatMessage> getHistory(UserChatRequest request) {
        // 1. 发送请求
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserChatRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<HashMap<String, List<ChatMessage>>> response = restTemplate.exchange(
                    chatHistoryUrl,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<HashMap<String, List<ChatMessage>>>() {
                    }
            );
            HashMap<String, List<ChatMessage>> responseMap = response.getBody();
            return responseMap != null ? responseMap.get("history") : Collections.emptyList();
        } catch (RuntimeException e) {
            log.error("获取历史记录失败: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<ChatConversation> getConversationList(Long userId) {
        LambdaQueryWrapper<ChatConversation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatConversation::getUserId, userId)
                .eq(ChatConversation::getDelFlag, 0)
                .orderByDesc(ChatConversation::getUpdateTime);
        return chatConversationMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteConversation(Long userId, String conversationId) {
        LambdaQueryWrapper<ChatConversation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatConversation::getUserId, userId)
                .eq(ChatConversation::getConversationId, conversationId)
                .eq(ChatConversation::getDelFlag, 0);
        
        ChatConversation conversation = chatConversationMapper.selectOne(queryWrapper);
        if (conversation == null) {
            throw new ServiceException("会话不存在或已删除");
        }
        
        // 软删除
        conversation.setDelFlag(1);
        chatConversationMapper.updateById(conversation);
    }

    @Override
    public void updateConversationTitle(Long userId, String conversationId, String title) {
        LambdaQueryWrapper<ChatConversation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatConversation::getUserId, userId)
                .eq(ChatConversation::getConversationId, conversationId)
                .eq(ChatConversation::getDelFlag, 0);
        
        ChatConversation conversation = chatConversationMapper.selectOne(queryWrapper);
        if (conversation == null) {
            throw new ServiceException("会话不存在或已删除");
        }
        
        // 更新标题
        conversation.setConversationTitle(title);
        chatConversationMapper.updateById(conversation);
    }

    private String getSessionId(String userId, String convId) {
        return userId + "_" + convId;
    }
}
