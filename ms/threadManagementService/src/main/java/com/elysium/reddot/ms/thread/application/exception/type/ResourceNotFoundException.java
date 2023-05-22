package com.elysium.reddot.ms.thread.application.exception.type;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super("The " + resourceType + " with ID " + resourceId + " does not exist.");
    }
}
