package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.thread;

import com.elysium.reddot.ms.thread.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.application.service.ThreadApplicationServiceImpl;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.application.data.mapper.ThreadDTOThreadModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * The GetThreadByIdProcessor class is a Camel Processor component that handles the retrieval of a thread by its ID.
 * It retrieves the thread using the ThreadApplicationServiceImpl and sets the response body with the retrieved thread.
 */
@Component
@AllArgsConstructor
@Slf4j
public class GetThreadByIdProcessor implements Processor {

    private final ThreadApplicationServiceImpl threadService;

    @Override
    public void process(Exchange exchange) {
        log.debug("Processing get thread by ID request...");

        Long inputId = Long.parseLong(exchange.getIn().getHeader("id").toString());

        ThreadModel threadModel = threadService.getThreadById(inputId);
        log.debug("Retrieved thread: {}", threadModel);

        ThreadDTO threadDTO = ThreadDTOThreadModel.toDTO(threadModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "Thread with id " + inputId + " retrieved successfully", threadDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);

        log.debug("Get thread by ID request processed successfully.");
    }

}