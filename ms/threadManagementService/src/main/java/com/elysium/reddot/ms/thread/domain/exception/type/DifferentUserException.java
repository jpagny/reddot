package com.elysium.reddot.ms.thread.domain.exception.type;

public class DifferentUserException extends RuntimeException {
    public DifferentUserException(String userFromMessageToUpdate, String userFromMessageExisting) {
        super(" User from message to update et user from message existing are not same : "
                + userFromMessageToUpdate + " - " + userFromMessageExisting);

    }
}
