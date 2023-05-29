package com.elysium.reddot.ms.board.infrastructure.data.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) to carry the response for a board existence check.
 */
@Data
public class BoardExistsResponseDTO {

    /**
     * Indicates whether the board exists or not.
     */
    private boolean exists;

}