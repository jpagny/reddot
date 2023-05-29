package com.elysium.reddot.ms.thread.application.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The ThreadDTO class represents a data transfer object for a thread.
 * It contains the attributes of a thread including its ID, name, label, description, board ID, and user ID.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreadDTO {

    @ApiModelProperty(notes = "The ID of the thread")
    private Long id;

    @ApiModelProperty(notes = "The name of the thread")
    private String name;

    @ApiModelProperty(notes = "The label of the thread")
    private String label;

    @ApiModelProperty(notes = "The description of the thread")
    private String description;

    @ApiModelProperty(notes = "The ID of the board the thread belongs to")
    private Long boardId;

    @ApiModelProperty(notes = "The ID of the user who created the thread")
    private String userId;
}
