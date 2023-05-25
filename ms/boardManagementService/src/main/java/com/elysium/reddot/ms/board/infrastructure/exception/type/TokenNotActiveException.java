package com.elysium.reddot.ms.board.infrastructure.exception.type;

public class TokenNotActiveException extends RuntimeException {
    public TokenNotActiveException() {
        super("Your token is inactive.");
    }
}


