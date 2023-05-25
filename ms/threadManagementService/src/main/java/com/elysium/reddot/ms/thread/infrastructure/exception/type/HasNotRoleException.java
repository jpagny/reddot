package com.elysium.reddot.ms.thread.infrastructure.exception.type;

public class HasNotRoleException extends RuntimeException {
    public HasNotRoleException(String role) {
        super("Having role " + role + " is required.");
    }
}
