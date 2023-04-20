package com.elysium.reddot.ms.user.application.exception.handler.core;

import org.apache.camel.Exchange;

public interface IExceptionHandler {
    void setNextHandler(IExceptionHandler nextHandler);
    void handleException(Exchange exchange, Exception exception);
}
