package com.elysium.reddot.ms.authentication.application.exception.handler.exceptionhandler;

import com.elysium.reddot.ms.authentication.application.exception.exception.KeycloakApiException;
import com.elysium.reddot.ms.authentication.application.exception.handler.core.AbstractIExceptionHandler;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class KeycloakApiExceptionHandler extends AbstractIExceptionHandler {

    @Override
    public void handleException(Exchange exchange, Exception exception) {
        if (exception instanceof KeycloakApiException) {
            processException(exchange, 500, exception.getMessage(), null);
        } else if (nextHandler != null) {
            nextHandler.handleException(exchange, exception);
        }
    }

}
