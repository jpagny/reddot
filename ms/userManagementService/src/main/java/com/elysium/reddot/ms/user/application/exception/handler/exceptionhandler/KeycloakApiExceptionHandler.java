package com.elysium.reddot.ms.user.application.exception.handler.exceptionHandler;

import com.elysium.reddot.ms.user.application.exception.exception.KeycloakApiException;
import com.elysium.reddot.ms.user.application.exception.handler.core.AbstractIExceptionHandler;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

/**
 * Exception handler for Keycloak API exceptions.
 * This handler specifically handles exceptions to type {@link KeycloakApiException}.
 * If the exception is not of that type, it passes the exception to the next handler in the chain.
 *
 * @see AbstractIExceptionHandler
 */
@Component
public class KeycloakApiExceptionHandler extends AbstractIExceptionHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleException(Exchange exchange, Exception exception) {
        if (exception instanceof KeycloakApiException) {
            processException(exchange, ((KeycloakApiException) exception).getStatus(), exception.getMessage());
        } else if (nextHandler != null) {
            nextHandler.handleException(exchange, exception);
        }
    }

}
