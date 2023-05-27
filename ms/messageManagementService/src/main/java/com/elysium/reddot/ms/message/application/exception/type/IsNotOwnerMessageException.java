package com.elysium.reddot.ms.message.application.exception.type;

public class IsNotOwnerMessageException extends RuntimeException {
    public IsNotOwnerMessageException() {
        super("You are not owner this message. You can't update this message");
    }
}
