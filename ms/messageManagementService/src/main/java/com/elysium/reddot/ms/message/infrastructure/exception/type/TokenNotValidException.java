package com.elysium.reddot.ms.message.infrastructure.exception.type;

public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException() {
        super("Token is not validate.");
    }
}