package com.elysium.reddot.ms.replymessage.domain.exception;

/**
 * This class represents a custom exception which is thrown when there is a mismatch
 * between the user trying to perform the update operation and the user who originally
 * created the existing message.
 * <p>
 * This exception is used to enforce the rule that only the creator of a message is allowed to update it.
 */
public class DifferentUserException extends RuntimeException {
    public DifferentUserException(String userFromMessageToUpdate, String userFromMessageExisting) {
        super(" User from message to update et user from message existing are not same : "
                + userFromMessageToUpdate + " - " + userFromMessageExisting);

    }
}
