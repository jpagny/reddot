package com.elysium.reddot.ms.board.infrastructure.data.exception;

public class HasNotRoleException extends RuntimeException {
    public HasNotRoleException(String role) {
        super("Having role " + role + " is required.");
    }
}
