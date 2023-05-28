package com.elysium.reddot.ms.replymessage.application.exception.type;

public class IsNotOwnerException extends RuntimeException {
    public IsNotOwnerException(String resource) {
        super("You are not owner this " + resource + ". You can't update this message");
    }
}
