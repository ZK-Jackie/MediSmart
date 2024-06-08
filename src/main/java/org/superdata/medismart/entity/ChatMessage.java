package org.superdata.medismart.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessage {
    private Long messageId;
    private Long userId;
    private Integer conversationId;
    private String userMessage;
    private String aiMessage;
    private Short isDeleted;
    private String createTime;
}
