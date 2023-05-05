package com.elysium.reddot.ms.message.domain.exception;

public class FieldWithSpaceException extends RuntimeException {
    public FieldWithSpaceException(String field) {
        super(field + " should not contain spaces. Replace them with '_' to fix this issue.");
    }
}