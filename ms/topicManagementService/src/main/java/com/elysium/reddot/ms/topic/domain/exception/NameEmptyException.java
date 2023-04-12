package com.elysium.reddot.ms.topic.domain.exception;

public class NameEmptyException extends RuntimeException {
    public NameEmptyException() {
        super("The name is required");
    }
}
