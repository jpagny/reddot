package com.elysium.reddot.ms.message.infrastructure.data.rabbitmq.received.response;

import lombok.Data;

@Data
public class ThreadExistsResponseDTO {
    private Long threadId;
    private boolean exists;
}