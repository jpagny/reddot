package com.elysium.reddot.ms.board.application.exception.handler.exceptionhandler;

import com.elysium.reddot.ms.board.application.exception.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.board.application.exception.handler.core.AbstractIExceptionHandler;
import org.apache.camel.Exchange;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ResourceAlreadyExistIExceptionHandler extends AbstractIExceptionHandler {

    @Override
    public void handleException(Exchange exchange, Exception exception) {
        if (exception instanceof ResourceAlreadyExistException) {
            processException(exchange, HttpStatus.CONFLICT.value(), exception.getMessage(), null);
        } else if (nextHandler != null) {
            nextHandler.handleException(exchange, exception);
        }
    }

}
