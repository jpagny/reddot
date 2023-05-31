package com.elysium.reddot.ms.authentication.application.data.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * Data Transfer Object (DTO) for carrying API response data.
 * It includes a status code, a message, and the actual data of the response.
 */
@Getter
@ApiModel(description = "Data Transfer Object for carrying API response data.")
public class ApiResponseDTO {

    @ApiModelProperty(notes = "The status code of the response.")
    private final int status;

    @ApiModelProperty(notes = "The message of the response.")
    private final String message;

    @ApiModelProperty(notes = "The actual data of the response.")
    private final Object data;

    /**
     * Constructs a new ApiResponseDTO with status, message, and data.
     *
     * @param status The status code of the response.
     * @param message The message of the response.
     * @param data The actual data of the response.
     */
    public ApiResponseDTO(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }


}