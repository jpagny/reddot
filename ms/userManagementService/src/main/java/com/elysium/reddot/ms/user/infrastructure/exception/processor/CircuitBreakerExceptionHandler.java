package com.elysium.reddot.ms.user.infrastructure.exception.processor;

import com.elysium.reddot.ms.user.infrastructure.exception.type.CircuitBreakerException;
import com.elysium.reddot.ms.user.infrastructure.exception.type.KeycloakApiException;
import com.elysium.reddot.ms.user.domain.exception.type.BadValueException;
import com.elysium.reddot.ms.user.infrastructure.data.exception.CircuitBreakerExceptionDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CircuitBreakerExceptionHandler implements Processor {

    @Override
    public void process(Exchange exchange) {
        log.debug("Processing circuit breaker exception...");

        int httpStatusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        CircuitBreakerException exception = (CircuitBreakerException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        Exception causeOfCircuitBreakException = exception.getCauseOfCircuitBreakException();
        log.debug("Cause of CircuitBreakerException: {}", causeOfCircuitBreakException.getClass().getSimpleName());

        if (causeOfCircuitBreakException instanceof KeycloakApiException
                || causeOfCircuitBreakException instanceof BadValueException) {
            httpStatusCode = HttpStatus.SC_BAD_REQUEST;
        }

        CircuitBreakerExceptionDTO response = new CircuitBreakerExceptionDTO();
        response.setStatus(httpStatusCode);
        response.setMessage(exception.getMessage());
        response.setExceptionClassName(causeOfCircuitBreakException.getClass().getSimpleName());
        response.setError(exception.getReasonMessage());
        log.debug("Created CircuitBreakerExceptionDTO: {}", response);

        exchange.getIn().setBody(response);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, httpStatusCode);
        log.debug("Set HTTP response code to {}", httpStatusCode);
    }

}
