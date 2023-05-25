package com.elysium.reddot.ms.replymessage.infrastructure.exception.type;

public class TokenNotActiveException extends RuntimeException {
    public TokenNotActiveException() {
        super("Your token is inactive.");
    }
}


