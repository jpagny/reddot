package com.elysium.reddot.ms.replymessage.infrastructure.data.rabbitmq.received.response;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for the response indicating whether a message exists.
 */
@Data
public class MessageExistsResponseDTO {
    private boolean exists;
}