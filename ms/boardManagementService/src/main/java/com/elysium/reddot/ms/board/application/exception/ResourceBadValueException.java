package com.elysium.reddot.ms.board.application.exception;

public class ResourceBadValueException extends RuntimeException{
    public ResourceBadValueException(String resourceType, String error) {
        super("The " + resourceType + " has bad value : " + error + ".");
    }
}
