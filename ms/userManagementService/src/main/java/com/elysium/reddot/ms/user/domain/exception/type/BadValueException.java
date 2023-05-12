package com.elysium.reddot.ms.user.domain.exception.type;

public class BadValueException extends RuntimeException{
    public BadValueException(String field, String rule) {
        super("Bad value in " + field + " : " + rule);
    }

}
