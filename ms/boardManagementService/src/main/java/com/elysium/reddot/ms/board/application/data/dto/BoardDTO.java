package com.elysium.reddot.ms.board.application.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * A Data Transfer Object (DTO) for Board.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    /**
     * The unique identifier of the board.
     */
    @ApiModelProperty(notes = "The unique identifier of the board. Auto-generated on creation.")
    private Long id;

    /**
     * The unique name of the board.
     */
    @ApiModelProperty(notes = "The unique name of the board.", required = true)
    private String name;

    /**
     * The label of the board, providing a brief tag or summary of the board's content.
     */
    @ApiModelProperty(notes = "The label of the board providing a brief tag or summary of the board's content.", required = true)
    private String label;

    /**
     * A detailed description of the board.
     */
    @ApiModelProperty(notes = "A detailed description of the board.")
    private String description;

    /**
     * The identifier of the associated topic. Links the board to a specific topic in the system.
     */
    @ApiModelProperty(notes = "The identifier of the associated topic. Links the board to a specific topic in the system.")
    private Long topicId;
}