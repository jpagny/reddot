package com.elysium.reddot.ms.topic.application.exception.handler;

import com.elysium.reddot.ms.topic.application.service.dto.ApiResponseDTO;
import org.apache.camel.Exchange;

public abstract class AbstractExceptionHandler implements ExceptionHandler {
    protected ExceptionHandler nextHandler;

    @Override
    public void setNextHandler(ExceptionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    protected void processException(Exchange exchange, int status, String message, Object data) {
        ApiResponseDTO apiResponse = new ApiResponseDTO(status, message, data);
        exchange.getIn().setBody(apiResponse);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, status);
    }
}
