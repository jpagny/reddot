package com.elysium.reddot.ms.message.infrastructure.data.rabbitmq.response;

import lombok.Data;

/**
 * A data class that represents the response for checking the existence of a message.
 */
@Data
public class MessageExistsResponseDTO {
    private boolean exists;
}