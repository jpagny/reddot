package com.elysium.reddot.ms.topic.application.exception;

import com.elysium.reddot.ms.topic.application.service.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.domain.exception.LabelEmptyException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;

public class CamelGlobalExceptionHandler implements Processor {

    @Override
    public void process(Exchange exchange) {
        Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        if (exception instanceof LabelEmptyException) {
            processException(exchange,HttpStatus.BAD_REQUEST.value(),exception.getMessage(),null);

        } else {

        }
    }

    private void processException(Exchange exchange, int status, String message, Object data){
        ApiResponseDTO apiResponse = new ApiResponseDTO(status, message, data);
        exchange.getIn().setBody(apiResponse);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, status);
    }
}