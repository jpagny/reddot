package com.elysium.reddot.ms.statistic.application.data;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class is a Data Transfer Object (DTO) for an API response.
 * It encapsulates the status, message, and data of a response.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO {

    /**
     * The status code of the API response.
     */
    private int status;

    /**
     * The message of the API response.
     */
    private String message;

    /**
     * The data of the API response.
     */
    private Object data;

}