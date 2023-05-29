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

import java.util.List;

/**
 * The GetAllThreadsProcessor class is a Camel Processor component that handles the retrieval of all threads.
 * It retrieves all threads using the ThreadApplicationServiceImpl and sets the response body with the retrieved threads.
 */
@Component
@AllArgsConstructor
@Slf4j
public class GetAllThreadsProcessor implements Processor {

    private final ThreadApplicationServiceImpl threadService;

    @Override
    public void process(Exchange exchange) {
        log.debug("Processing get all threads request...");

        List<ThreadModel> listThreadsModel = threadService.getAllThreads();

        List<ThreadDTO> listThreadsDTO = ThreadDTOThreadModel.toDTOList(listThreadsModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "All threads retrieved successfully", listThreadsDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);

        log.debug("Get all threads request processed successfully.");
    }
}
