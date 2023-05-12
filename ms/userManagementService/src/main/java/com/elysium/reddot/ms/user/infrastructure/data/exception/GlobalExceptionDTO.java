package com.elysium.reddot.ms.user.infrastructure.data.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalExceptionDTO {
    String message;
    String exceptionClass;
}
