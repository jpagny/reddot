package com.elysium.reddot.ms.authentication.infrastructure.exception.processor;

import com.elysium.reddot.ms.authentication.infrastructure.data.exception.GlobalExceptionDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GlobalExceptionHandler implements Processor {

    /**
     * Processes a global exception by creating a GlobalExceptionDTO with the exception's details.
     *
     * <p>This method retrieves the exception that caused the global exception from the Exchange's properties.
     * It then creates a GlobalExceptionDTO with the exception's simple class name and message,
     * and sets it as the body of the Exchange's input message. The HTTP response code is set to
     * INTERNAL_SERVER_ERROR (500).
     *
     * @param exchange the Exchange containing the exception to be processed and the output GlobalExceptionDTO
     */
    @Override
    public void process(Exchange exchange) {
        log.debug("Processing global exception...");

        int httpStatusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        Exception causeOfGlobalException = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        log.debug("Cause of global exception : {}", causeOfGlobalException.getClass().getSimpleName());

        GlobalExceptionDTO response = new GlobalExceptionDTO(
                causeOfGlobalException.getClass().getSimpleName(),
                causeOfGlobalException.getMessage());
        log.debug("Created GlobalExceptionDTO: {}", response);

        exchange.getIn().setBody(response);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, httpStatusCode);
        log.debug("Set HTTP response code to {}", httpStatusCode);
    }
}
