package com.elysium.reddot.ms.message.infrastructure.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalExceptionDTO {
    String exceptionClass;
    String message;
}
