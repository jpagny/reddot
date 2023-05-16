package com.elysium.reddot.ms.board.infrastructure.exception.processor;

import com.elysium.reddot.ms.board.application.exception.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.board.application.exception.exception.ResourceBadValueException;
import com.elysium.reddot.ms.board.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.board.infrastructure.data.exception.GlobalExceptionDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

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
