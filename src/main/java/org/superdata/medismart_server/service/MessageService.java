package org.superdata.medismart.service;

import org.superdata.medismart.entity.request.UserChatRequest;

public interface MessageService {
    void saveChatMessage(UserChatRequest userChatRequest, String aiMessage);
}
