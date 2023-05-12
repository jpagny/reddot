package com.elysium.reddot.ms.user.infrastructure.exception.processor;

import com.elysium.reddot.ms.user.infrastructure.data.exception.GlobalExceptionDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;

@Component
@Slf4j
public class GlobalExceptionHandler implements Processor {

    @Override
    public void process(Exchange exchange) {
        log.debug("Processing global exception...");

        int httpStatusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        Exception causeOfGlobalException = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        log.debug("Cause of global exception : {}", causeOfGlobalException.getClass().getSimpleName());

        if (causeOfGlobalException instanceof NotFoundException) {
            httpStatusCode = HttpStatus.SC_NOT_FOUND;

        } else if (causeOfGlobalException instanceof BadRequestException) {
            httpStatusCode = HttpStatus.SC_BAD_REQUEST;

        } else if (causeOfGlobalException instanceof NotAllowedException) {
            httpStatusCode = HttpStatus.SC_METHOD_NOT_ALLOWED;

        }

        GlobalExceptionDTO response = new GlobalExceptionDTO();
        response.setExceptionClass(causeOfGlobalException.getClass().getSimpleName());
        response.setMessage(causeOfGlobalException.getMessage());
        log.debug("Created GlobalExceptionDTO: {}", response);

        exchange.getIn().setBody(response);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, httpStatusCode);
        log.debug("Set HTTP response code to {}", httpStatusCode);
    }
}
