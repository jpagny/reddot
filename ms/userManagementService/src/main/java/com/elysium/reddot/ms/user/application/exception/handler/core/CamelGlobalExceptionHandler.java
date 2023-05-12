package com.elysium.reddot.ms.user.application.exception.handler.core;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;

/**
 * Global exception handler for Camel routes.
 * <p>
 * This class acts as a Camel Processor and is responsible for handling exceptions that occur within Camel routes.
 * It uses an exception handler chain to handle different types of exceptions.
 *
 * @see Processor
 */
public class CamelGlobalExceptionHandler implements Processor {
    private final IExceptionHandler exceptionHandlerChain;

    /**
     * Constructs a CamelGlobalExceptionHandler with the provided list of exception handlers.
     *
     * @param exceptionHandlers the list of exception handlers to be used in the exception handler chain
     */
    public CamelGlobalExceptionHandler(List<IExceptionHandler> exceptionHandlers) {
        this.exceptionHandlerChain = buildExceptionHandlerChain(exceptionHandlers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(Exchange exchange) {
        Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        exceptionHandlerChain.handleException(exchange, exception);
    }

    /**
     * Builds the exception handler chain from the list of exception handlers.
     *
     * @param exceptionHandlers the list of exception handlers
     * @return the exception handler chain
     */
    private IExceptionHandler buildExceptionHandlerChain(List<IExceptionHandler> exceptionHandlers) {
        for (int i = 0; i < exceptionHandlers.size() - 1; i++) {
            exceptionHandlers.get(i).setNextHandler(exceptionHandlers.get(i + 1));
        }
        return exceptionHandlers.get(0);
    }

}
