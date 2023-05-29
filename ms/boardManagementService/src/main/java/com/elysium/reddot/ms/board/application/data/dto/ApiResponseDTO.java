package com.elysium.reddot.ms.board.application.data.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class is a Data Transfer Object (DTO) for an API response.
 * It encapsulates the status, message, and data of a response.
 */
@Getter
@RequiredArgsConstructor
@ApiModel(description = "Data Transfer Object representing an API response")
public class ApiResponseDTO {

    /**
     * The status code of the API response.
     */
    @ApiModelProperty("The status code of the API response")
    private final int status;

    /**
     * The message of the API response.
     */
    @ApiModelProperty("The message of the API response")
    private final String message;

    /**
     * The data of the API response.
     */
    @ApiModelProperty("The data of the API response")
    private final Object data;

}