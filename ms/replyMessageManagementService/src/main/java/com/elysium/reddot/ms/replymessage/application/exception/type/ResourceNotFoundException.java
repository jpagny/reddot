package com.elysium.reddot.ms.replymessage.application.exception.type;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super("The " + resourceType + " with ID " + resourceId + " does not exist.");

    }
}
