package com.elysium.reddot.ms.thread.infrastructure.exception.type;

public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException() {
        super("Token is not validate.");
    }
}