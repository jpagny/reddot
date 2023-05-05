package com.elysium.reddot.ms.thread.infrastructure.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BoardExistsResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long boardId;
    private boolean exists;
}
