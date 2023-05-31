package com.elysium.reddot.ms.board.application.exception.type;

/**
 * This class represents a custom exception that should be thrown when a requested resource is not found in the system.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new exception with a detailed message.
     *
     * @param resourceType The type of the resource that was not found.
     * @param resourceId The ID of the resource that was not found.
     */
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super("The " + resourceType + " with ID " + resourceId + " does not exist.");
    }
}
