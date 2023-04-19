package com.elysium.reddot.ms.board.application.data.dto;


import lombok.Getter;

@Getter
public class ApiResponseDTO {

    private final int status;
    private final String message;
    private final Object data;

    public ApiResponseDTO(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }


}