package org.superdata.medismart;

import cn.hutool.json.JSONUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.superdata.medismart.entity.request.UserChatRequest;
import org.superdata.medismart.service.ChatService;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestChat {

    @Autowired
    private ChatService chatService;

    @Test
    void testGetHistory() {
        System.out.println(chatService.getHistory(new UserChatRequest("2", "", "1")));
    }

    @Test
    void testStreamChatMessage() throws Exception {
        // 准备测试数据
        UserChatRequest request = new UserChatRequest();
        request.setContent("你好");
        request.setUserId("1");
        request.setConversationId("1");

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "text/event-stream");
        headers.add("token", "test-token");

        HttpEntity<UserChatRequest> requestEntity = new HttpEntity<>(request, headers);

        // 使用RestTemplate发送请求并获取SSE流
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.exchange(
                "http://localhost:" + 8081 + "/stream",
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );

        // 读取SSE流
        List<String> messages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(response.getBody())))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("data:")) {
                    String message = line.substring(5).trim();
                    messages.add(message);
                    System.out.println("Received: " + message);
                }
            }
        }

        // 验证结果
        assertTrue(messages.size() > 0, "应该收到至少一条消息");
        messages.forEach(msg -> {
            assertTrue(msg.length() > 0, "消息不应该为空");
            System.out.println("验证消息: " + msg);
        });
    }

    @Test
    void testPythonConnection() throws InterruptedException {
        // 准备测试数据
        UserChatRequest userChatRequest = new UserChatRequest();
        userChatRequest.setContent("头痛怎么办");
        userChatRequest.setUserId("1");
        userChatRequest.setConversationId("7");

        CountDownLatch latch = new CountDownLatch(1);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        EventSource.Factory factory = EventSources.createFactory(client);
        RequestBody body = RequestBody.create(
                JSONUtil.toJsonStr(userChatRequest),
                okhttp3.MediaType.parse("application/json; charset=utf-8")
        );
        Request request = new Request.Builder()
                .url("http://localhost:8080/stream")
                .post(body)
                .header("Accept", "text/event-stream")
                .header("Cache-Control", "no-cache")
                .header("Connection", "keep-alive")
                .build();

        EventSourceListener eventSourceListener = new EventSourceListener() {
            @Override
            public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
                System.out.println("连接已打开");
            }

            @Override
            public void onEvent(@NotNull EventSource eventSource, String id, String type, String data) {
                System.out.println("实时收到消息块: " + data);
            }

            @Override
            public void onFailure(@NotNull EventSource eventSource, Throwable t, Response response) {
                if (t != null) {
                    System.out.println("连接失败: " + t.getMessage());
                }
                latch.countDown();
            }

            @Override
            public void onClosed(@NotNull EventSource eventSource) {
                System.out.println("连接已关闭");
                latch.countDown();
            }
        };

        EventSource eventSource = factory.newEventSource(request, eventSourceListener);

        // 等待20秒或直到连接关闭/失败
        boolean completed = latch.await(20, TimeUnit.SECONDS);

        // 输出结果统计
        System.out.println("\n=== 测试结果 ===");
        System.out.println("是否正常完成: " + completed);

        // 关闭连接
        eventSource.cancel();
    }
}