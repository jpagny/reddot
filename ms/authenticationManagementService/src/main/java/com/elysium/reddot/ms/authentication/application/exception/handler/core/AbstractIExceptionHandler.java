package com.elysium.reddot.ms.authentication.application.exception.handler.core;

import com.elysium.reddot.ms.authentication.application.data.dto.ApiResponseDTO;
import org.apache.camel.Exchange;

public abstract class AbstractIExceptionHandler implements IExceptionHandler {
    protected IExceptionHandler nextHandler;

    @Override
    public void setNextHandler(IExceptionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    protected void processException(Exchange exchange, int status, String message, Object data) {
        ApiResponseDTO apiResponse = new ApiResponseDTO(status, message, data);
        exchange.getIn().setBody(apiResponse);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, status);
    }
}
