package com.elysium.reddot.ms.message.infrastructure.data;

import lombok.Data;

@Data
public class MessageExistsResponseDTO {
    private static final long serialVersionUID = 1L;
    private Long parentMessageID;
    private boolean exists;
}