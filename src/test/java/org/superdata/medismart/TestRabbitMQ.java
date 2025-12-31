package org.superdata.medismart;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.superdata.medismart.common.constant.Constants;
import org.superdata.medismart.entity.request.UserChatRequest;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@SpringBootTest
public class TestRabbitMQ {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    void testSend(){
        rabbitTemplate.convertAndSend(Constants.STOCK_EX, Constants.STOCK_ROUT, "hello rabbitmq");
    }

    @Test
    void testReceive(){
//        String replyQueueName = rabbitTemplate.execute(channel -> {
//            return channel.queueDeclare().getQueue();
//        });
        UserChatRequest userChatRequest = new UserChatRequest();
        userChatRequest.setUserId("1");
        userChatRequest.setContent("你知道我叫什么名字的吧");
        userChatRequest.setConversationId("1");
//        ObjectMapper objectMapper = new ObjectMapper(userChatRequest);
//        String jsonMessage = objectMapper.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = null;
        try {
            jsonMessage = objectMapper.writeValueAsString(userChatRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(jsonMessage);
        rabbitTemplate.convertAndSend(Constants.STOCK_EX, Constants.STOCK_ROUT, jsonMessage);

//        String aiMessage = (String) rabbitTemplate.receiveAndConvert("response",500);

        Message response = null;
        while (response == null) {
            response = rabbitTemplate.receive("response", 500); // Wait for up to 5 seconds for a message
        }
        String aiMessage = new String(response.getBody(), StandardCharsets.UTF_8);
        //        rabbitTemplate.execute(channel -> {
//            channel.queueDelete(replyQueueName);
//            return null;
//        });
        System.out.println(aiMessage);
    }
}
