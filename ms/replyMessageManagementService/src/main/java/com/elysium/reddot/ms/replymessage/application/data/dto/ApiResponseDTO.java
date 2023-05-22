package com.elysium.reddot.ms.replymessage.application.data.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO {

    private int status;
    private String message;
    private Object data;

}