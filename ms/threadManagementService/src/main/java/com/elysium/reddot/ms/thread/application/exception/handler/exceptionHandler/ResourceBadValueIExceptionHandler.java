package com.elysium.reddot.ms.thread.application.exception.handler.exceptionHandler;

import com.elysium.reddot.ms.thread.application.exception.exception.ResourceBadValueException;
import com.elysium.reddot.ms.thread.application.exception.handler.core.AbstractIExceptionHandler;
import org.apache.camel.Exchange;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ResourceBadValueIExceptionHandler extends AbstractIExceptionHandler {
    @Override
    public void handleException(Exchange exchange, Exception exception) {
        if (exception instanceof ResourceBadValueException) {
            processException(exchange, HttpStatus.BAD_REQUEST.value(), exception.getMessage(), null);
        } else if (nextHandler != null) {
            nextHandler.handleException(exchange, exception);
        }
    }
}
