package com.elysium.reddot.ms.user.infrastructure.data.exception;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for carrying exception information.
 * It includes exception class name and the exception message.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalExceptionDTO {

    /**
     * The class name of the exception.
     */
    @ApiModelProperty(notes = "The class name of the exception.")
    private String exceptionClass;

    /**
     * The message associated with the exception.
     */
    @ApiModelProperty(notes = "The message associated with the exception.")
    private String message;
}
