package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.thread;

import com.elysium.reddot.ms.thread.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.application.service.KeycloakService;
import com.elysium.reddot.ms.thread.application.service.ThreadApplicationServiceImpl;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.application.data.mapper.ThreadDTOThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.outbound.rabbitmq.requester.BoardExistRequester;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.io.IOException;

/**
 * The CreateThreadProcessor class is a Camel Processor component that handles the creation of a thread.
 * It verifies the existence of the board ID and creates the thread using the ThreadApplicationServiceImpl.
 */
@Component
@AllArgsConstructor
@Slf4j
public class CreateThreadProcessor implements Processor {

    private final ThreadApplicationServiceImpl threadApplicationService;
    private final KeycloakService keycloakService;
    private final BoardExistRequester boardExistRequester;

    @Override
    public void process(Exchange exchange) throws IOException, AuthenticationException {
        log.debug("Processing create thread request...");

        ThreadDTO inputThreadDTO = exchange.getIn().getBody(ThreadDTO.class);
        log.debug("Received input ThreadDTO: {}", inputThreadDTO);

        // add user id
        String userId = keycloakService.getUserId();
        inputThreadDTO.setUserId(userId);
        log.debug("Adding userId: {}", inputThreadDTO);

        ThreadModel threadModel = ThreadDTOThreadModel.toModel(inputThreadDTO);

        log.debug("Verifying existence of board ID: {}", threadModel.getBoardId());
        boardExistRequester.verifyBoardIdExistsOrThrow(threadModel.getBoardId());

        createThreadAndSetResponse(exchange, threadModel);

        log.debug("Create thread request processed successfully.");
    }

    private void createThreadAndSetResponse(Exchange exchange, ThreadModel threadModel) {
        log.debug("Creating thread using ThreadApplicationService...");

        ThreadModel createdThreadModel = threadApplicationService.createThread(threadModel);

        ThreadDTO createdThreadDTO = ThreadDTOThreadModel.toDTO(createdThreadModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Thread with name " + createdThreadModel.getName() + " created successfully", createdThreadDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
        exchange.getMessage().setBody(apiResponseDTO);

        log.debug("Created thread: {}", createdThreadDTO);
        log.debug("Set HTTP response code to {}", HttpStatus.CREATED.value());
    }

}