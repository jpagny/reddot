package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.thread.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.application.service.ThreadApplicationServiceImpl;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.mapper.ThreadProcessorMapper;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GetThreadByIdProcessor implements Processor {

    private final ThreadApplicationServiceImpl threadService;

    @Override
    public void process(Exchange exchange) {
        Long inputId = Long.parseLong(exchange.getIn().getHeader("id").toString());

        ThreadModel threadModel = threadService.getThreadById(inputId);

        ThreadDTO threadDTO = ThreadProcessorMapper.toDTO(threadModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "Thread with id " + inputId + " retrieved successfully", threadDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }

}