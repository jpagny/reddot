package com.elysium.reddot.ms.replymessage.infrastructure.data.rabbitMQ.response;

import lombok.Data;

@Data
public class MessageExistsResponseDTO {
    private static final long serialVersionUID = 1L;
    private Long parentMessageID;
    private boolean exists;
}