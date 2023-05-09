package com.elysium.reddot.ms.replymessage.infrastructure.data;

import lombok.Data;

@Data
public class MessageExistsResponseDTO {
    private static final long serialVersionUID = 1L;
    private Long boardId;
    private boolean exists;
}