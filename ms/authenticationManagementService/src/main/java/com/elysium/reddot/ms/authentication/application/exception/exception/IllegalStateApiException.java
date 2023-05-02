package com.elysium.reddot.ms.authentication.application.exception.exception;

public class IllegalStateApiException extends RuntimeException {
    public IllegalStateApiException(String message, Exception ex) {
        super(String.format("API error: %s. Exception message: %s", message, ex.getMessage()), ex);
    }
}
