package com.elysium.reddot.ms.message.infrastructure.data.rabbitmq.received.response;

import lombok.Data;

/**
 * A data class that represents the response for checking the existence of a thread.
 */
@Data
public class ThreadExistsResponseDTO {
    private boolean exists;
}