package com.elysium.reddot.ms.message.application.exception.exception;

public class ResourceBadValueException extends RuntimeException{
    public ResourceBadValueException(String resourceType, String error) {
        super("The " + resourceType + " has bad value : " + error + ".");
    }
}
