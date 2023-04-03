package com.reddot.ms.topic.exception;

public class ResourceNotFoundException extends Exception{
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super("The " + resourceType + " with ID " + resourceId + " does not exist.");
    }
}
