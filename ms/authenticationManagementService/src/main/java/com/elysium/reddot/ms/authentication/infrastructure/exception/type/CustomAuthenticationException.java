package com.elysium.reddot.ms.authentication.infrastructure.exception.type;

public class CustomAuthenticationException extends RuntimeException {
    public CustomAuthenticationException(String message, Throwable cause) {
        super(message);
        System.out.println("TESTTTTooo");
    }
}
