package com.elysium.reddot.ms.board.application.exception.exception;

public class ResourceAlreadyExistException extends RuntimeException {
    public ResourceAlreadyExistException(String resourceType, String fieldName, String resourceName) {
        super("The " + resourceType + " with " + fieldName + " '" + resourceName + "' already exists.");
    }
}