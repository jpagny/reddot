package com.elysium.reddot.ms.message.infrastructure.data.rabbitMQ.received.response;

import lombok.Data;

@Data
public class ThreadExistsResponseDTO {
    private static final long serialVersionUID = 1L;
    private Long boardId;
    private boolean exists;
}