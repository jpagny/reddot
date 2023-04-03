package com.reddot.ms.topic.exception;

public class ResourceAlreadyExistException extends RuntimeException  {
    public ResourceAlreadyExistException(String resourceType, String resourceName, String fieldName) {
        super("The " + resourceType + " with " + fieldName + " '" + resourceName + "' already exists");
    }
}