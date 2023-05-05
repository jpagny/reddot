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
public class DeleteThreadProcessor implements Processor {

    private final ThreadApplicationServiceImpl threadApplicationService;

    @Override
    public void process(Exchange exchange) {
        Long inputId = exchange.getIn().getHeader("id", Long.class);

        ThreadModel deletedThreadModel = threadApplicationService.deleteThreadById(inputId);

        ThreadDTO deletedThreadDTO = ThreadProcessorMapper.toDTO(deletedThreadModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Thread with id " + inputId + " deleted successfully", deletedThreadDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }
}