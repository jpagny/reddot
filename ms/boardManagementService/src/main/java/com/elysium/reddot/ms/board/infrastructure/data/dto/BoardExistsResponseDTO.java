package com.elysium.reddot.ms.board.infrastructure.data.dto;

import lombok.Data;

@Data
public class BoardExistsResponseDTO {
    private Long boardId;
    private boolean exists;
}