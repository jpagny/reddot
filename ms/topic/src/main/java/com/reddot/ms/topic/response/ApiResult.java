package com.reddot.ms.topic.response;

import lombok.Data;

@Data
public class ApiResult {
    private String message;
    private Object data;

    public ApiResult(String message) {
        this.message = message;
    }

    public ApiResult(String message, Object data) {
        this.message = message;
        this.data = data;
    }

}