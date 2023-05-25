package com.elysium.reddot.ms.topic.domain.exception.type;

public class FieldEmptyException extends RuntimeException {
    public FieldEmptyException(String fieldName) {
        super(fieldName + " is required and cannot be empty");
    }
}