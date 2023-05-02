package com.elysium.reddot.ms.authentication.application.exception.handler.exceptionhandler;

import com.elysium.reddot.ms.authentication.application.exception.exception.IllegalStateApiException;
import com.elysium.reddot.ms.authentication.application.exception.handler.core.AbstractIExceptionHandler;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class IllegalStateExceptionHandler extends AbstractIExceptionHandler {

    @Override
    public void handleException(Exchange exchange, Exception exception) {
        if (exception instanceof IllegalStateApiException) {
            processException(exchange, 500, exception.getMessage(), null);
        } else if (nextHandler != null) {
            nextHandler.handleException(exchange, exception);
        }
    }

}

