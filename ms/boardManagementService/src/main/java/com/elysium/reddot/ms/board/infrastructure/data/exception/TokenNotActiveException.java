package com.elysium.reddot.ms.board.infrastructure.data.exception;

public class TokenNotActiveException extends RuntimeException {
    public TokenNotActiveException() {
        super("Your token is inactive.");
    }
}


