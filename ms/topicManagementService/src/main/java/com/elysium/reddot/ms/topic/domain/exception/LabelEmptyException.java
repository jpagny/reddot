package com.elysium.reddot.ms.topic.domain.exception;

public class LabelEmptyException extends RuntimeException {
    public LabelEmptyException() {
        super("Label is required");
    }
}
