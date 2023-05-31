package com.elysium.reddot.ms.thread.application.exception.type;

/**
 * This class represents a custom exception that should be thrown when a resource in the system
 * has a bad or invalid value.
 */
public class ResourceBadValueException extends RuntimeException{

    /**
     * Constructs a new exception with a detailed message.
     *
     * @param resourceType The type of the resource that has the bad value.
     * @param error The specific error or bad value associated with the resource.
     */
    public ResourceBadValueException(String resourceType, String error) {
        super("The " + resourceType + " has bad value : " + error + ".");
    }
}
