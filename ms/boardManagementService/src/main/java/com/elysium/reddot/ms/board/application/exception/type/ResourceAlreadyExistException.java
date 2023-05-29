package com.elysium.reddot.ms.board.application.exception.type;

/**
 * This class represents a custom exception that should be thrown when an attempt is made
 * to create a resource that already exists in the system.
 */
public class ResourceAlreadyExistException extends RuntimeException {

    /**
     * Constructs a new exception with a detailed message.
     *
     * @param resourceType The type of the resource that already exists.
     * @param fieldName The field name that is used to check the existence of the resource.
     * @param resourceName The name of the resource that already exists.
     */
    public ResourceAlreadyExistException(String resourceType, String fieldName, String resourceName) {
        super("The " + resourceType + " with " + fieldName + " '" + resourceName + "' already exists.");
    }
}