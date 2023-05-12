package com.elysium.reddot.ms.user.application.exception.handler.core;

import com.elysium.reddot.ms.user.application.data.dto.ApiResponseDTO;
import org.apache.camel.Exchange;

/**
 * Abstract class for exception handlers.
 * <p>
 * This class provides a basic implementation of an exception handler chain.
 * Each handler in the chain is responsible for handling a specific type of exception.
 * If a handler cannot handle an exception, it passes the exception to the next handler in the chain.
 * <p>
 * Subclasses should implement the {@link IExceptionHandler#handleException} method to provide specific handling for different exceptions.
 */
public abstract class AbstractIExceptionHandler implements IExceptionHandler {
    protected IExceptionHandler nextHandler;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNextHandler(IExceptionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * Processes an exception by setting the response body and HTTP response code on the exchange.
     *
     * @param exchange the exchange where the response will be set
     * @param status the HTTP status code to be set on the response
     * @param message the message to be set on the response
     */
    protected void processException(Exchange exchange, int status, String message) {
        ApiResponseDTO apiResponse = new ApiResponseDTO(status, message, null);
        exchange.getIn().setBody(apiResponse);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, status);
    }
}
