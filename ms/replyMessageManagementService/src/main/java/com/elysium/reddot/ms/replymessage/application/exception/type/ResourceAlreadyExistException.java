package com.elysium.reddot.ms.replymessage.application.exception.type;

public class ResourceAlreadyExistException extends RuntimeException {
    public ResourceAlreadyExistException(String resourceType, String fieldName, String resourceName) {
        super("The " + resourceType + " with " + fieldName + " '" + resourceName + "' already exists.");
    }
}