package com.elysium.reddot.ms.topic.application.exception.handler.exceptionhandler;

import com.elysium.reddot.ms.topic.application.exception.exception.ResourceBadValueException;
import com.elysium.reddot.ms.topic.application.exception.handler.core.AbstractExceptionHandler;
import org.apache.camel.Exchange;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ResourceBadValueExceptionHandler extends AbstractExceptionHandler {
    @Override
    public void handleException(Exchange exchange, Exception exception) {
        if (exception instanceof ResourceBadValueException) {
            processException(exchange, HttpStatus.BAD_REQUEST.value(), exception.getMessage(), null);
        } else if (nextHandler != null) {
            nextHandler.handleException(exchange, exception);
        }
    }
}
