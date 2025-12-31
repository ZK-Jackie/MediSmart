package org.superdata.medismart.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.superdata.medismart.base.BaseTest;
import org.superdata.medismart.common.ResponseResult;
import org.superdata.medismart.entity.ChatConversation;
import org.superdata.medismart.entity.ChatMessage;
import org.superdata.medismart.entity.request.UserChatRequest;
import org.superdata.medismart.security.domain.LoginUser;
import org.superdata.medismart.service.ChatService;
import org.superdata.medismart.utils.TestDataFactory;
import org.superdata.medismart.utils.database.RedisCache;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 聊天控制器测试类
 */
@SpringBootTest
@DisplayName("聊天控制器测试")
class ChatControllerTest extends BaseTest {

    @Mock
    private ChatService chatService;

    @Mock
    private RedisCache redisCache;

    @InjectMocks
    private ChatController chatController;

    private MockMvc mockMvc;
    private UserChatRequest chatRequest;
    private LoginUser loginUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
        chatRequest = TestDataFactory.createChatRequest();
        loginUser = new LoginUser();
        loginUser.setUser(TestDataFactory.createTestUser());

        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(loginUser);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("连接聊天 - 成功")
    void testConnect_success() {
        // 准备测试数据
        String sessionId = "test-session-id";
        SseEmitter expectedEmitter = new SseEmitter();

        // Mock行为
        when(chatService.connect(sessionId)).thenReturn(expectedEmitter);

        // 执行测试
        SseEmitter result = chatController.connect(sessionId, null);

        // 验证结果
        assertNotNull(result);
        assertEquals(expectedEmitter, result);
        verify(chatService, times(1)).connect(sessionId);
    }

    @Test
    @DisplayName("流式聊天 - 成功")
    void testStreamChatMessage_success() {
        // 准备测试数据
        Map<String, Object> streamResult = new HashMap<>();
        streamResult.put("sessionId", "test-session-id");
        streamResult.put("conversationId", "test-conversation-id");

        // Mock行为
        when(chatService.stream(any(UserChatRequest.class))).thenReturn(streamResult);

        // 执行测试
        ResponseResult result = chatController.streamChatMessage(chatRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) result.getData();
        assertTrue(data.containsKey("sessionId"));
        assertTrue(data.containsKey("conversationId"));

        // 验证userId被设置
        verify(chatService, times(1)).stream(any(UserChatRequest.class));
    }

    @Test
    @DisplayName("获取聊天历史 - 成功")
    void testGetChatHistory_success() {
        // 准备测试数据
        String conversationId = "test-conversation-id";
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage message1 = new ChatMessage();
        message1.setType("user");
        message1.setContent("你好");
        messages.add(message1);

        // Mock行为
        when(chatService.getHistory(any(UserChatRequest.class))).thenReturn(messages);

        // 执行测试
        ResponseResult result = chatController.getChatHistory(conversationId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        
        @SuppressWarnings("unchecked")
        List<ChatMessage> data = (List<ChatMessage>) result.getData();
        assertNotNull(data);
        assertEquals(1, data.size());
        assertEquals("user", data.get(0).getType());

        verify(chatService, times(1)).getHistory(any(UserChatRequest.class));
    }

    @Test
    @DisplayName("获取聊天历史 - 空历史")
    void testGetChatHistory_empty() {
        // 准备测试数据
        String conversationId = "test-conversation-id";

        // Mock行为
        when(chatService.getHistory(any(UserChatRequest.class))).thenReturn(Collections.emptyList());

        // 执行测试
        ResponseResult result = chatController.getChatHistory(conversationId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        
        @SuppressWarnings("unchecked")
        List<ChatMessage> data = (List<ChatMessage>) result.getData();
        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    @DisplayName("获取会话列表 - 成功")
    void testGetConversationList_success() {
        // 准备测试数据
        List<ChatConversation> conversations = Arrays.asList(
                TestDataFactory.createChatConversation(),
                TestDataFactory.createChatConversation()
        );

        // Mock行为
        when(chatService.getConversationList(anyLong())).thenReturn(conversations);

        // 执行测试
        ResponseResult result = chatController.getConversationList();

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        
        @SuppressWarnings("unchecked")
        List<ChatConversation> data = (List<ChatConversation>) result.getData();
        assertNotNull(data);
        assertEquals(2, data.size());

        verify(chatService, times(1)).getConversationList(anyLong());
    }

    @Test
    @DisplayName("获取会话列表 - 空列表")
    void testGetConversationList_empty() {
        // Mock行为
        when(chatService.getConversationList(anyLong())).thenReturn(Collections.emptyList());

        // 执行测试
        ResponseResult result = chatController.getConversationList();

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        
        @SuppressWarnings("unchecked")
        List<ChatConversation> data = (List<ChatConversation>) result.getData();
        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    @DisplayName("删除会话 - 成功")
    void testDeleteConversation_success() {
        // 准备测试数据
        String conversationId = "test-conversation-id";

        // Mock行为
        doNothing().when(chatService).deleteConversation(anyLong(), anyString());

        // 执行测试
        ResponseResult result = chatController.deleteConversation(conversationId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("删除成功", result.getData());

        verify(chatService, times(1)).deleteConversation(anyLong(), eq(conversationId));
    }

    @Test
    @DisplayName("更新会话标题 - 成功")
    void testUpdateConversationTitle_success() {
        // 准备测试数据
        String conversationId = "test-conversation-id";
        String newTitle = "新的标题";

        // Mock行为
        doNothing().when(chatService).updateConversationTitle(anyLong(), anyString(), anyString());

        // 执行测试
        ResponseResult result = chatController.updateConversationTitle(conversationId, newTitle);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("更新成功", result.getData());

        verify(chatService, times(1)).updateConversationTitle(anyLong(), eq(conversationId), eq(newTitle));
    }

    @Test
    @DisplayName("更新会话标题 - 空标题")
    void testUpdateConversationTitle_emptyTitle() {
        // 准备测试数据
        String conversationId = "test-conversation-id";
        String newTitle = "";

        // Mock行为
        doNothing().when(chatService).updateConversationTitle(anyLong(), anyString(), anyString());

        // 执行测试
        ResponseResult result = chatController.updateConversationTitle(conversationId, newTitle);

        // 验证结果
        assertNotNull(result);
        verify(chatService, times(1)).updateConversationTitle(anyLong(), eq(conversationId), eq(newTitle));
    }
}
