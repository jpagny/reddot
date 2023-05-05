package com.elysium.reddot.ms.thread.domain.exception;

public class FieldEmptyException extends RuntimeException {
    public FieldEmptyException(String fieldName) {
        super(fieldName + " is required and cannot be empty");
    }
}