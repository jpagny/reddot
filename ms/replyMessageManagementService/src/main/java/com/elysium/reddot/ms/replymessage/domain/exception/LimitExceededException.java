package com.elysium.reddot.ms.replymessage.domain.exception;

/**
 * Exception class to handle situations where a defined limit has been exceeded.
 * This exception is a type of unchecked exception as it extends RuntimeException.
 */
public class LimitExceededException extends RuntimeException {

    public LimitExceededException(String message) {
        super(message);
    }
}
