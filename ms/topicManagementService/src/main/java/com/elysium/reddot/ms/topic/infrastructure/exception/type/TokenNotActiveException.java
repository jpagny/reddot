package com.elysium.reddot.ms.topic.infrastructure.exception.type;

public class TokenNotActiveException extends RuntimeException {
    public TokenNotActiveException() {
        super("Your token is inactive.");
    }
}


