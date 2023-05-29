package com.elysium.reddot.ms.board.domain.exception.type;

/**
 * Exception thrown when a field contains space characters.
 * This class extends RuntimeException, meaning it is an unchecked exception.
 * Unchecked exceptions do not need to be declared in a method's or a constructor's throws clause.
 */
public class FieldWithSpaceException extends RuntimeException {

    /**
     * Constructs a new FieldWithSpaceException with the detailed message.
     *
     * @param field the name of the field that contains spaces. This field is used in the
     *              exception message to indicate which field contains spaces.
     */
    public FieldWithSpaceException(String field) {
        super(field + " should not contain spaces. Replace them with '_' to fix this issue.");
    }
}