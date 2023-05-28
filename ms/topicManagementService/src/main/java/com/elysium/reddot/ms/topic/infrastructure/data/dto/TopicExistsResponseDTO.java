package com.elysium.reddot.ms.topic.infrastructure.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing the response of a topic existence check.
 */
@Data
public class TopicExistsResponseDTO {

    /**
     * Indicates whether the topic exists or not.
     */
    @ApiModelProperty(value = "Indicates whether the topic exists or not")
    private boolean exists;
}
