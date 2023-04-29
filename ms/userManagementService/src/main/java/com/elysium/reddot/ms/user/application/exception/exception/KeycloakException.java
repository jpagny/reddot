package com.elysium.reddot.ms.user.application.exception.exception;

public class KeycloakException extends RuntimeException {
    public KeycloakException(String exception) {
        super(exception);
    }
}