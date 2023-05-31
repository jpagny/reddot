package com.elysium.reddot.ms.message.domain.exception;

/**
 * This class represents a custom exception which is thrown when a required field
 * in an entity or a data transfer object (DTO) is empty.
 * <p>
 * This exception is used to enforce data integrity by ensuring that all required fields are provided.
 */
public class FieldEmptyException extends RuntimeException {
    public FieldEmptyException(String fieldName) {
        super(fieldName + " is required and cannot be empty");
    }
}