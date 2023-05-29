package com.elysium.reddot.ms.board.infrastructure.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) to carry the response for a topic existence check.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicExistsResponseDTO {

    /**
     * Indicates whether the topic exists or not.
     */
    private boolean exists;

}