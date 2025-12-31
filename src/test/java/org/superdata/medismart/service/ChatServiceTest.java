package org.superdata.medismart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import okhttp3.Request;
import okhttp3.sse.EventSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.superdata.medismart.base.BaseTest;
import org.superdata.medismart.common.exception.ServiceException;
import org.superdata.medismart.entity.ChatConversation;
import org.superdata.medismart.entity.ChatMessage;
import org.superdata.medismart.entity.request.UserChatRequest;
import org.superdata.medismart.mapper.sql.ChatConversationMapper;
import org.superdata.medismart.service.impl.ChatServiceImpl;
import org.superdata.medismart.utils.TestDataFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 聊天服务测试类
 */
@SpringBootTest
@DisplayName("聊天服务测试")
class ChatServiceTest extends BaseTest {

    @Mock
    private EventSource.Factory eventSourceFactory;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ChatConversationMapper chatConversationMapper;

    @InjectMocks
    private ChatServiceImpl chatService;

    private UserChatRequest testChatRequest;
    private ChatConversation testConversation;

    @BeforeEach
    void setUp() {
        testChatRequest = TestDataFactory.createChatRequest();
        testConversation = TestDataFactory.createChatConversation();
    }

    @Test
    @DisplayName("连接聊天 - 通过sessionId")
    void testConnect_withSessionId() {
        // 准备测试数据
        String sessionId = "test-session-id";

        // 执行测试
        SseEmitter result = chatService.connect(sessionId);

        // 验证结果
        assertNotNull(result);
    }

    @Test
    @DisplayName("流式聊天 - 新会话")
    void testStream_newConversation() {
        // 准备测试数据
        UserChatRequest request = TestDataFactory.createNewConversationChatRequest();
        EventSource eventSource = mock(EventSource.class);

        // Mock行为
        when(chatConversationMapper.insert(any(ChatConversation.class))).thenReturn(1);
        when(eventSourceFactory.newEventSource(any(Request.class), any())).thenReturn(eventSource);

        // 执行测试
        Map<String, Object> result = chatService.stream(request);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.containsKey("sessionId"));
        assertTrue(result.containsKey("conversationId"));
        assertNotNull(result.get("sessionId"));
        assertNotNull(result.get("conversationId"));

        // 验证会话被创建
        verify(chatConversationMapper, times(1)).insert(any(ChatConversation.class));
    }

    @Test
    @DisplayName("流式聊天 - 已存在的会话")
    void testStream_existingConversation() {
        // 准备测试数据
        UserChatRequest request = TestDataFactory.createChatRequest();
        EventSource eventSource = mock(EventSource.class);

        // Mock行为
        when(eventSourceFactory.newEventSource(any(Request.class), any())).thenReturn(eventSource);

        // 执行测试
        Map<String, Object> result = chatService.stream(request);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.containsKey("sessionId"));
        assertTrue(result.containsKey("conversationId"));

        // 验证没有创建新会话
        verify(chatConversationMapper, never()).insert(any(ChatConversation.class));
    }

    @Test
    @DisplayName("流式聊天 - 参数为空")
    void testStream_nullRequest() {
        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            chatService.stream(null);
        });

        assertEquals("输入不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("流式聊天 - 用户ID为空")
    void testStream_nullUserId() {
        // 准备测试数据
        UserChatRequest request = TestDataFactory.createChatRequest();
        request.setUserId(null);

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            chatService.stream(request);
        });

        assertEquals("输入不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("获取聊天历史 - 成功")
    void testGetHistory_success() {
        // 准备测试数据
        UserChatRequest request = TestDataFactory.createChatRequest();
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage message1 = new ChatMessage();
        message1.setType("user");
        message1.setContent("你好");
        messages.add(message1);

        Map<String, List<ChatMessage>> responseMap = new HashMap<>();
        responseMap.put("history", messages);

        // Mock行为
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(org.springframework.core.ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(responseMap));

        // 执行测试
        List<ChatMessage> result = chatService.getHistory(request);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user", result.get(0).getType());
        assertEquals("你好", result.get(0).getContent());
    }

    @Test
    @DisplayName("获取聊天历史 - 请求失败")
    void testGetHistory_failure() {
        // 准备测试数据
        UserChatRequest request = TestDataFactory.createChatRequest();

        // Mock行为 - 模拟请求失败
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(org.springframework.core.ParameterizedTypeReference.class)
        )).thenThrow(new RuntimeException("连接失败"));

        // 执行测试
        List<ChatMessage> result = chatService.getHistory(request);

        // 验证结果 - 应返回空列表
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("获取会话列表 - 成功")
    void testGetConversationList_success() {
        // 准备测试数据
        Long userId = 1L;
        List<ChatConversation> conversations = Arrays.asList(
                testConversation,
                TestDataFactory.createChatConversation()
        );

        // Mock行为
        when(chatConversationMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(conversations);

        // 执行测试
        List<ChatConversation> result = chatService.getConversationList(userId);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(chatConversationMapper, times(1)).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取会话列表 - 空列表")
    void testGetConversationList_empty() {
        // 准备测试数据
        Long userId = 1L;

        // Mock行为
        when(chatConversationMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Collections.emptyList());

        // 执行测试
        List<ChatConversation> result = chatService.getConversationList(userId);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("删除会话 - 成功")
    void testDeleteConversation_success() {
        // 准备测试数据
        Long userId = 1L;
        String conversationId = testConversation.getConversationId();

        // Mock行为
        when(chatConversationMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(testConversation);
        when(chatConversationMapper.updateById(any(ChatConversation.class)))
                .thenReturn(1);

        // 执行测试
        assertDoesNotThrow(() -> {
            chatService.deleteConversation(userId, conversationId);
        });

        // 验证删除标志被设置
        assertEquals(1, testConversation.getDelFlag());
        verify(chatConversationMapper, times(1)).updateById(testConversation);
    }

    @Test
    @DisplayName("删除会话 - 会话不存在")
    void testDeleteConversation_notFound() {
        // 准备测试数据
        Long userId = 1L;
        String conversationId = "non-existent-id";

        // Mock行为
        when(chatConversationMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(null);

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            chatService.deleteConversation(userId, conversationId);
        });

        assertEquals("会话不存在或已删除", exception.getMessage());
        verify(chatConversationMapper, never()).updateById(any(ChatConversation.class));
    }

    @Test
    @DisplayName("更新会话标题 - 成功")
    void testUpdateConversationTitle_success() {
        // 准备测试数据
        Long userId = 1L;
        String conversationId = testConversation.getConversationId();
        String newTitle = "新的标题";

        // Mock行为
        when(chatConversationMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(testConversation);
        when(chatConversationMapper.updateById(any(ChatConversation.class)))
                .thenReturn(1);

        // 执行测试
        assertDoesNotThrow(() -> {
            chatService.updateConversationTitle(userId, conversationId, newTitle);
        });

        // 验证标题被更新
        assertEquals(newTitle, testConversation.getConversationTitle());
        verify(chatConversationMapper, times(1)).updateById(testConversation);
    }

    @Test
    @DisplayName("更新会话标题 - 会话不存在")
    void testUpdateConversationTitle_notFound() {
        // 准备测试数据
        Long userId = 1L;
        String conversationId = "non-existent-id";
        String newTitle = "新的标题";

        // Mock行为
        when(chatConversationMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(null);

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            chatService.updateConversationTitle(userId, conversationId, newTitle);
        });

        assertEquals("会话不存在或已删除", exception.getMessage());
        verify(chatConversationMapper, never()).updateById(any(ChatConversation.class));
    }
}
