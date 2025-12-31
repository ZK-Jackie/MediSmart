package org.superdata.medismart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.superdata.medismart.common.ResponseResult;
import org.superdata.medismart.common.constant.CacheConstants;
import org.superdata.medismart.entity.request.UserChatRequest;
import org.superdata.medismart.service.ChatService;
import org.superdata.medismart.utils.database.RedisCache;
import org.superdata.medismart.utils.security.SecurityUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
public class ChatController {

    @Resource
    private ChatService chatService;

    @Resource
    private RedisCache redisCache;

    /**
     * 连接聊天
     *
     * @return SseEmitter
     */
    @GetMapping(value = "/connect/{sessionId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@PathVariable String sessionId, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Content-Type", "text/event-stream;charset=UTF-8");
        response.setHeader("X-Accel-Buffering", "no");
        return chatService.connect(sessionId);
    }


    /**
     * 流式聊天
     *
     * @param userChatRequest 用户聊天请求
     * @return 响应结果
     */
    @PostMapping("/stream")
    public ResponseResult streamChatMessage(@RequestBody UserChatRequest userChatRequest) {
        String userId = String.valueOf(SecurityUtils.getUserId());
        userChatRequest.setUserId(userId);

        return ResponseResult.success(chatService.stream(userChatRequest));
    }


    /**
     * 获取聊天记录
     *
     * @param conversationId 会话id
     * @return 聊天记录
     */
    @GetMapping("/chat/{conversationId}")
    public ResponseResult getChatHistory(@PathVariable String conversationId) {
        String userId = String.valueOf(SecurityUtils.getUserId());
        return ResponseResult.success(chatService.getHistory(new UserChatRequest(userId, "", conversationId)));
    }

    /**
     * 获取用户的会话列表
     *
     * @return 会话列表
     */
    @GetMapping("/conversations")
    public ResponseResult getConversationList() {
        Long userId = SecurityUtils.getUserId();
        return ResponseResult.success(chatService.getConversationList(userId));
    }

    /**
     * 删除会话
     *
     * @param conversationId 会话id
     * @return 响应结果
     */
    @DeleteMapping("/conversation/{conversationId}")
    public ResponseResult deleteConversation(@PathVariable String conversationId) {
        Long userId = SecurityUtils.getUserId();
        chatService.deleteConversation(userId, conversationId);
        return ResponseResult.success("删除成功");
    }

    /**
     * 更新会话标题
     *
     * @param conversationId 会话id
     * @param title 新标题
     * @return 响应结果
     */
    @PutMapping("/conversation/{conversationId}/title")
    public ResponseResult updateConversationTitle(@PathVariable String conversationId, @RequestParam String title) {
        Long userId = SecurityUtils.getUserId();
        chatService.updateConversationTitle(userId, conversationId, title);
        return ResponseResult.success("更新成功");
    }

}
