package com.elysium.reddot.ms.replymessage.infrastructure.exception.type;

public class HasNotRoleException extends RuntimeException {
    public HasNotRoleException(String role) {
        super("Having role " + role + " is required.");
    }
}
