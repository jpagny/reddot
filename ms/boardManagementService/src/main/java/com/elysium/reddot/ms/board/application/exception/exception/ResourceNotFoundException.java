package com.elysium.reddot.ms.board.application.exception.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super("The " + resourceType + " with ID " + resourceId + " does not exist.");
    }
}
