package com.elysium.reddot.ms.topic.domain.exception.type;

/**
 * Exception thrown when a required field is empty.
 * This class extends RuntimeException, meaning it is an unchecked exception.
 * Unchecked exceptions do not need to be declared in a method's or a constructor's throws clause.
 */
public class FieldEmptyException extends RuntimeException {

    /**
     * Constructs a new FieldEmptyException with the detailed message.
     *
     * @param fieldName the name of the field that is empty. This field is used in the
     *                  exception message to indicate which field is empty.
     */
    public FieldEmptyException(String fieldName) {
        super(fieldName + " is required and cannot be empty");
    }
}