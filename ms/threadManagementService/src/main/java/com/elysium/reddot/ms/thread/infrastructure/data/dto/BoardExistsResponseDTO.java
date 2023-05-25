package com.elysium.reddot.ms.thread.infrastructure.data.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BoardExistsResponseDTO implements Serializable {
    private Long boardId;
    private boolean exists;
}
