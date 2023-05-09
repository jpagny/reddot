package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.thread.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.application.service.ThreadApplicationServiceImpl;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.mapper.ThreadProcessorMapper;
import com.elysium.reddot.ms.thread.infrastructure.outbound.rabbitMQ.requester.BoardExistRequester;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class CreateThreadProcessor implements Processor {

    private final ThreadApplicationServiceImpl threadApplicationService;
    private final BoardExistRequester boardExistRequester;

    @Override
    public void process(Exchange exchange) {

        ThreadDTO inputThreadDTO = exchange.getIn().getBody(ThreadDTO.class);
        ThreadModel threadModel = ThreadProcessorMapper.toModel(inputThreadDTO);

        boardExistRequester.verifyBoardIdExistsOrThrow(threadModel.getBoardId());
        // add userId
        threadModel.setUserId("1");

        createThreadAndSetResponse(exchange, threadModel);
    }

    private void createThreadAndSetResponse(Exchange exchange, ThreadModel threadModel) {
        ThreadModel createdThreadModel = threadApplicationService.createThread(threadModel);
        ThreadDTO createdThreadDTO = ThreadProcessorMapper.toDTO(createdThreadModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Thread with name " + createdThreadModel.getName() + " created successfully", createdThreadDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }

}