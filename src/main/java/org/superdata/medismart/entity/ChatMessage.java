package org.superdata.medismart.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    String type;

    String content;
}
