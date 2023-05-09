package com.elysium.reddot.ms.replymessage.domain.exception;

public class LimitExceededException extends RuntimeException {

    public LimitExceededException(String message) {
        super(message);
    }
}
