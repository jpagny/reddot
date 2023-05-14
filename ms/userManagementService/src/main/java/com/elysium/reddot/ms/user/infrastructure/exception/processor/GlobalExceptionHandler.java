package com.elysium.reddot.ms.user.infrastructure.exception.processor;

import com.elysium.reddot.ms.user.domain.exception.type.BadValueException;
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
