package com.elysium.reddot.ms.replymessage.application.exception.handler.core;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;

public class CamelGlobalExceptionHandler implements Processor {
    private final IExceptionHandler exceptionHandlerChain;

    public CamelGlobalExceptionHandler(List<IExceptionHandler> exceptionHandlers) {
        this.exceptionHandlerChain = buildExceptionHandlerChain(exceptionHandlers);
    }

    @Override
    public void process(Exchange exchange) {
        Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        exceptionHandlerChain.handleException(exchange, exception);
    }

    private IExceptionHandler buildExceptionHandlerChain(List<IExceptionHandler> exceptionHandlers) {
        for (int i = 0; i < exceptionHandlers.size() - 1; i++) {
            exceptionHandlers.get(i).setNextHandler(exceptionHandlers.get(i + 1));
        }
        return exceptionHandlers.get(0);
    }

}
