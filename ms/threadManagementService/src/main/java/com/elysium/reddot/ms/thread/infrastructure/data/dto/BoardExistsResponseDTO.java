package com.elysium.reddot.ms.thread.infrastructure.data.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * The BoardExistsResponseDTO class represents the response structure for checking if a board exists.
 * It contains a boolean value indicating if the board exists or not.
 */
@Data
public class BoardExistsResponseDTO {
    private boolean exists;
}
