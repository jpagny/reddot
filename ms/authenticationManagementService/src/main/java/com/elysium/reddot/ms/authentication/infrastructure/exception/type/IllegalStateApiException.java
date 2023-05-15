package com.elysium.reddot.ms.authentication.infrastructure.exception.type;

public class IllegalStateApiException extends RuntimeException {
    public IllegalStateApiException(String message, Exception ex) {
        super(String.format("API error: %s. Exception message: %s", message, ex.getMessage()), ex);
    }
}
