package com.elysium.reddot.ms.thread.infrastructure.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The GlobalExceptionDTO class represents a data transfer object for global exceptions.
 * It contains the exception class name and the exception message.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalExceptionDTO {
    String exceptionClass;
    String message;
}
