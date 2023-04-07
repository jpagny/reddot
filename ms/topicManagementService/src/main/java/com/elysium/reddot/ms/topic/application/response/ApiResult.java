package com.elysium.reddot.ms.topic.application.response;


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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}