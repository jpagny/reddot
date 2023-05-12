package com.elysium.reddot.ms.user.application.data.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data Transfer Object (DTO) class representing an API response.
 * This class encapsulates the status, message, and data of an API response.
 */
@Getter
@AllArgsConstructor
public class ApiResponseDTO {
    private final int status;
    private final String message;
    private final Object data;
}