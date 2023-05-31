package com.elysium.reddot.ms.replymessage.domain.exception;

/**
 * This class represents a custom exception which is thrown when a required field
 * in an entity or a data transfer object (DTO) is empty.
 */
public class FieldEmptyException extends RuntimeException {
    public FieldEmptyException(String fieldName) {
        super(fieldName + " is required and cannot be empty");
    }
}