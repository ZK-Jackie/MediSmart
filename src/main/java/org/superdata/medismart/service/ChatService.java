package org.superdata.medismart.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.superdata.medismart.entity.ChatConversation;
import org.superdata.medismart.entity.ChatMessage;
import org.superdata.medismart.entity.request.UserChatRequest;

import java.util.List;
import java.util.Map;

public interface ChatService {
    SseEmitter connect(String sessionId);

    SseEmitter connect(String userId, String convId);

    Map<String, Object> stream(UserChatRequest userChatRequest);

    List<ChatMessage> getHistory(UserChatRequest request);

    List<ChatConversation> getConversationList(Long userId);

    void deleteConversation(Long userId, String conversationId);

    void updateConversationTitle(Long userId, String conversationId, String title);
}
