package org.superdata.medismart.controller;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;
import org.superdata.medismart.common.ResponseResult;
import org.superdata.medismart.constant.CacheConstants;
import org.superdata.medismart.constant.Constants;
import org.superdata.medismart.entity.request.UserChatRequest;
import org.superdata.medismart.service.MessageService;
import org.superdata.medismart.utils.JwtUtil;
import org.superdata.medismart.utils.RedisCache;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class ChatController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisCache redisCache;

//    @Resource
//    private MessageService messageService;

    /**
     * 发送聊天消息
     * @param userChatRequest 用户聊天请求
     * @param request 请求
     * @return ai回答
     */
    @PostMapping("/chat")
    public ResponseResult sendChatMessage(@RequestBody UserChatRequest userChatRequest, HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = JwtUtil.getUserId(token);

        HashMap<String, Object> data = new HashMap<>();

        userChatRequest.setUserId(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = null;
        try {
            jsonMessage = objectMapper.writeValueAsString(userChatRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        rabbitTemplate.convertAndSend(Constants.STOCK_EX, Constants.STOCK_ROUT, jsonMessage);
        Message response = null;
        while (response == null) {
            response = rabbitTemplate.receive("response", 500);
        }
        String aiMessage = new String(response.getBody(), StandardCharsets.UTF_8);
        data.put("output", aiMessage);
//        messageService.saveChatMessage(userChatRequest, aiMessage);
        return new ResponseResult<>(200, "ai回答", data);
    }


    /**
     * 获取聊天记录
     * @param conversationId 会话id
     * @param request 请求
     * @return 聊天记录
     */
    @GetMapping("/chat/{conversationId}")
    public ResponseResult getChatHistory(@PathVariable String conversationId, HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = JwtUtil.getUserId(token);

        String chatKey = CacheConstants.USER_CHAT_KEY + userId + ":" + conversationId;
        List<Object> cacheList = redisCache.getCacheList(chatKey);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object jsonTree = mapper.readTree(cacheList.toString());
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            String formattedJson = writer.writeValueAsString(jsonTree);
            System.out.println(formattedJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ResponseResult(200, "获取聊天记录成功", cacheList);
    }

}
