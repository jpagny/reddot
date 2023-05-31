package com.elysium.reddot.ms.user.domain.exception.type;

/**
 * This class represents an exception that is thrown when a bad value is encountered.
 */
public class BadValueException extends RuntimeException{

    /**
     * Constructs a new BadValueException with a detailed message.
     *
     * @param field The field where the bad value was encountered.
     * @param rule The rule that was violated.
     */
    public BadValueException(String field, String rule) {
        super("Bad value in " + field + " : " + rule);
    }

}
