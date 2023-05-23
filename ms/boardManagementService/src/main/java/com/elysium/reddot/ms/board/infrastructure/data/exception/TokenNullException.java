package com.elysium.reddot.ms.board.infrastructure.data.exception;

public class TokenNullException extends RuntimeException {
    public TokenNullException() {
        super("Token is null or empty. Token is required.");
    }
}