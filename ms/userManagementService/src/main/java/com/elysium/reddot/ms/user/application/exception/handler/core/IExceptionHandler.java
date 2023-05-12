package com.elysium.reddot.ms.user.application.exception.handler.core;

import org.apache.camel.Exchange;

public interface IExceptionHandler {

    /**
     * Sets the next handler in the chain.
     *
     * @param nextHandler the next handler in the chain
     */
    void setNextHandler(IExceptionHandler nextHandler);

    /**
     * Handles the specified exception within the given Camel exchange.
     * <p>
     * This method is responsible for handling the exception and performing any necessary operations,
     * such as logging, transforming the response, or setting appropriate headers.
     * The specific behavior of exception handling is defined by the implementation of this method
     * in concrete classes that implement the {@link IExceptionHandler} interface.
     *
     * @param exchange the Camel exchange where the exception occurred
     * @param exception the exception to be handled
     */
    void handleException(Exchange exchange, Exception exception);
}
