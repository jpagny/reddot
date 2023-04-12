package com.elysium.reddot.ms.topic.application.exception.handler.exceptionhandler;

import com.elysium.reddot.ms.topic.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.application.exception.handler.core.AbstractExceptionHandler;
import org.apache.camel.Exchange;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ResourceNotFoundExceptionHandler extends AbstractExceptionHandler {

    @Override
    public void handleException(Exchange exchange, Exception exception) {
        if (exception instanceof ResourceNotFoundException) {
            processException(exchange, HttpStatus.NOT_FOUND.value(), exception.getMessage(), null);
        } else if (nextHandler != null) {
            nextHandler.handleException(exchange, exception);
        }
    }

}
