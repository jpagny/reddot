package com.elysium.reddot.ms.thread.application.exception.type;

/**
 * This exception is thrown when a user attempts to update a message they do not own.
 */
public class IsNotOwnerMessageException extends RuntimeException {
    public IsNotOwnerMessageException() {
        super("You are not owner this message. You can't update this message");
    }
}
