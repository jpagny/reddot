package com.elysium.reddot.ms.user.infrastructure.exception.type;

import lombok.Getter;

@Getter
public class CircuitBreakerException extends RuntimeException {

    private final String message;
    private final Exception causeOfCircuitBreakException ;
    private final String reasonMessage;
    public CircuitBreakerException(String message, Exception causeOfCircuitBreakException ) {
        super(message);
        this.message = message;
        this.causeOfCircuitBreakException  = causeOfCircuitBreakException ;
        this.reasonMessage = causeOfCircuitBreakException .getMessage();
    }

}
