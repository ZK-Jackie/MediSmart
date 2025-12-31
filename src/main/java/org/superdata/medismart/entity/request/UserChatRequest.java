package org.superdata.medismart.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserChatRequest {

    private String userId;

    private String content;

    private String conversationId;
}
