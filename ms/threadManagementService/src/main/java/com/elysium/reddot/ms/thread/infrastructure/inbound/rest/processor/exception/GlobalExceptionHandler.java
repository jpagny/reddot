package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.exception;

import com.elysium.reddot.ms.thread.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.thread.application.exception.type.ResourceBadValueException;
import com.elysium.reddot.ms.thread.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.thread.infrastructure.data.dto.GlobalExceptionDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * The GlobalExceptionHandler class is a Camel Processor component that handles global exceptions.
 * It processes the exception and sets the appropriate HTTP response code and response body.
 */
@Component
@Slf4j
public class GlobalExceptionHandler implements Processor {

    @Override
    public void process(Exchange exchange) {
        log.debug("Processing global exception...");

        int httpStatusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        Exception causeOfGlobalException = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        log.debug("Cause of global exception : {}", causeOfGlobalException.getClass().getSimpleName());

        if (causeOfGlobalException instanceof ResourceAlreadyExistException) {
            httpStatusCode = HttpStatus.SC_CONFLICT;

        } else if (causeOfGlobalException instanceof ResourceBadValueException) {
            httpStatusCode = HttpStatus.SC_BAD_REQUEST;

        } else if (causeOfGlobalException instanceof ResourceNotFoundException) {
            httpStatusCode = HttpStatus.SC_NOT_FOUND;

        }

        GlobalExceptionDTO response = new GlobalExceptionDTO(
                causeOfGlobalException.getClass().getSimpleName(),
                causeOfGlobalException.getMessage());
        log.debug("Created GlobalExceptionDTO: {}", response);

        exchange.getIn().setBody(response);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, httpStatusCode);
        log.debug("Set HTTP response code to {}", httpStatusCode);
    }
}
