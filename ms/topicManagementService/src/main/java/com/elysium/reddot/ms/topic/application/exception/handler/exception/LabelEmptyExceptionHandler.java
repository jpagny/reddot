package com.elysium.reddot.ms.topic.application.exception.handler.exception;

import com.elysium.reddot.ms.topic.application.exception.handler.AbstractExceptionHandler;
import com.elysium.reddot.ms.topic.domain.exception.LabelEmptyException;
import org.apache.camel.Exchange;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class LabelEmptyExceptionHandler extends AbstractExceptionHandler {

    @Override
    public void handleException(Exchange exchange, Exception exception) {
        if (exception instanceof LabelEmptyException) {
            processException(exchange, HttpStatus.BAD_REQUEST.value(), exception.getMessage(), null);
        } else if (nextHandler != null) {
            nextHandler.handleException(exchange, exception);
        }
    }

}
