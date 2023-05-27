package com.elysium.reddot.ms.message.domain.exception;

import java.io.IOException;

public class DifferentUserException extends RuntimeException {
    public DifferentUserException(String userFromMessageToUpdate, String userFromMessageExisting) {
        super(" User from message to update et user from message existing are not same : "
                + userFromMessageToUpdate + " - " + userFromMessageExisting);

    }
}
