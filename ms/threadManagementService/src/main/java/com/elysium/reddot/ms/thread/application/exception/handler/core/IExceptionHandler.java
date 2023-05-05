package com.elysium.reddot.ms.thread.application.exception.handler.core;

import org.apache.camel.Exchange;

public interface IExceptionHandler {
    void setNextHandler(IExceptionHandler nextHandler);
    void handleException(Exchange exchange, Exception exception);
}
