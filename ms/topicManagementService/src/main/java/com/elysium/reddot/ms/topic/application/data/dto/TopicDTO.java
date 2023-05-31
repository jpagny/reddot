package com.elysium.reddot.ms.topic.application.data.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is a Data Transfer Object (DTO) for a Topic.
 * It is used to encapsulate the data of a Topic for transportation between processes or across network.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Data Transfer Object representing a Topic")
public class TopicDTO {

    /**
     * The unique identifier of the topic.
     */
    @ApiModelProperty("The unique identifier of the topic")
    private Long id;

    /**
     * The name of the topic.
     */
    @ApiModelProperty("The name of the topic")
    private String name;

    /**
     * The label of the topic.
     */
    @ApiModelProperty("The label of the topic")
    private String label;

    /**
     * The description of the topic.
     */
    @ApiModelProperty("The description of the topic")
    private String description;
}