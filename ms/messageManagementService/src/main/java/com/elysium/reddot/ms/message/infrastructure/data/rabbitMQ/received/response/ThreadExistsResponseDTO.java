package com.elysium.reddot.ms.message.infrastructure.data.rabbitmq.received.response;

import lombok.Data;

@Data
public class ThreadExistsResponseDTO {
    private boolean exists;
}