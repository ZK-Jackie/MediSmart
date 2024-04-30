package org.superdata.medismart.entity.request;

import lombok.Data;

@Data
public class UserChatRequest {

    private String userId;

    private String content;

    private String conversationId;
}
