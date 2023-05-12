package com.elysium.reddot.ms.user.application.exception.handler.exceptionHandler;

import com.elysium.reddot.ms.user.application.exception.handler.core.AbstractIExceptionHandler;
import com.elysium.reddot.ms.user.domain.exception.BadValueException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.camel.Exchange;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Exception handler for JsonProcessingException.
 * This handler specifically handles exceptions to type {@link JsonProcessingException}.
 * If the exception is not of that type, it passes the exception to the next handler in the chain.
 *
 * @see AbstractIExceptionHandler
 */
@Component
public class JsonProcessingExceptionHandler extends AbstractIExceptionHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleException(Exchange exchange, Exception exception) {
        if (exception instanceof BadValueException) {
            processException(exchange, HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        } else if (nextHandler != null) {
            nextHandler.handleException(exchange, exception);
        }
    }

}