package com.elysium.reddot.ms.topic.application.exception.handler;

import com.elysium.reddot.ms.topic.application.exception.handler.ExceptionHandler;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;

public class CamelGlobalExceptionHandler implements Processor {
    private final ExceptionHandler exceptionHandlerChain;

    public CamelGlobalExceptionHandler(List<ExceptionHandler> exceptionHandlers) {
        this.exceptionHandlerChain = buildExceptionHandlerChain(exceptionHandlers);
    }

    @Override
    public void process(Exchange exchange) {
        Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        exceptionHandlerChain.handleException(exchange, exception);
    }

    private ExceptionHandler buildExceptionHandlerChain(List<ExceptionHandler> exceptionHandlers) {
        for (int i = 0; i < exceptionHandlers.size() - 1; i++) {
            exceptionHandlers.get(i).setNextHandler(exceptionHandlers.get(i + 1));
        }
        return exceptionHandlers.get(0);
    }


}
