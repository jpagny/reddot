package com.elysium.reddot.ms.user.infrastructure.inbound.rest.processor.exception;

import com.elysium.reddot.ms.user.domain.exception.type.BadValueException;
import com.elysium.reddot.ms.user.infrastructure.data.exception.GlobalExceptionDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * This is a global exception handler that implements Camel's Processor interface.
 * It is annotated as a Spring component (@Component) and uses the Slf4j logger.
 * <p>
 * The handler processes all exceptions and returns a GlobalExceptionDTO
 * object along with an appropriate HTTP status code.
 */
@Component
@Slf4j
public class GlobalExceptionHandler implements Processor {

    /**
     * Processes the exception and prepares the HTTP response.
     *
     * @param exchange the Camel exchange object that contains the exception.
     */
    @Override
    public void process(Exchange exchange) {
        log.debug("Processing global exception...");

        int httpStatusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        Exception causeOfGlobalException = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        log.debug("Cause of global exception : {}", causeOfGlobalException.getClass().getSimpleName());

        if (causeOfGlobalException instanceof BadValueException) {
            httpStatusCode = HttpStatus.SC_BAD_REQUEST;

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
