package com.elysium.reddot.ms.message.application.data.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO {

    private int status;
    private String message;
    private Object data;

}