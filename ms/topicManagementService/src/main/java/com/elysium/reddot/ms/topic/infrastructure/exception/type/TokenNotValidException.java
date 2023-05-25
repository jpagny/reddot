package com.elysium.reddot.ms.topic.infrastructure.exception.type;

public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException() {
        super("Token is not validate.");
    }
}