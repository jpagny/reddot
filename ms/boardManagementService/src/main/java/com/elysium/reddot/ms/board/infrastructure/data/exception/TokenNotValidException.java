package com.elysium.reddot.ms.board.infrastructure.data.exception;

public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException() {
        super("Token is not validate.");
    }
}