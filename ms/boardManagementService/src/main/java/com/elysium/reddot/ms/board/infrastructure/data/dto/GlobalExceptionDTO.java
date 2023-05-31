package com.elysium.reddot.ms.board.infrastructure.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for representing a global exception.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalExceptionDTO {

    /**
     * The class name of the exception.
     */
    @ApiModelProperty("The class name of the exception")
    String exceptionClass;

    /**
     * The error message of the exception.
     */
    @ApiModelProperty("The error message of the exception")
    String message;
}
