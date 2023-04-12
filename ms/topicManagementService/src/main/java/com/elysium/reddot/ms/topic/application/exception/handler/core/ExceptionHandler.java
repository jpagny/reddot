package com.elysium.reddot.ms.topic.application.exception.handler.core;

import org.apache.camel.Exchange;

public interface ExceptionHandler {
    void setNextHandler(ExceptionHandler nextHandler);
    void handleException(Exchange exchange, Exception exception);
}
