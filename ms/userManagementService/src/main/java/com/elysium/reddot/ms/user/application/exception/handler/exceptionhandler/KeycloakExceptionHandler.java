package com.elysium.reddot.ms.user.application.exception.handler.exceptionhandler;

import com.elysium.reddot.ms.user.application.exception.exception.KeycloakException;
import com.elysium.reddot.ms.user.application.exception.handler.core.AbstractIExceptionHandler;
import org.apache.camel.Exchange;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class KeycloakExceptionHandler extends AbstractIExceptionHandler {

    @Override
    public void handleException(Exchange exchange, Exception exception) {
        if (exception instanceof KeycloakException) {
            processException(exchange, HttpStatus.BAD_REQUEST.value(), exception.getMessage(), null);
        } else if (nextHandler != null) {
            nextHandler.handleException(exchange, exception);
        }
    }

}
