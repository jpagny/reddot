package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.thread;

import com.elysium.reddot.ms.thread.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.application.service.KeycloakService;
import com.elysium.reddot.ms.thread.application.service.ThreadApplicationServiceImpl;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.application.data.mapper.ThreadDTOThreadModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;

/**
 * The UpdateThreadProcessor class is a Camel Processor component that handles the update of a thread.
 * It updates the thread using the ThreadApplicationServiceImpl and sets the response body with the updated thread.
 */
@Component
@AllArgsConstructor
@Slf4j
public class UpdateThreadProcessor implements Processor {

    private final ThreadApplicationServiceImpl threadService;
    private final KeycloakService keycloakService;

    @Override
    public void process(Exchange exchange) throws AuthenticationException {
        log.debug("Processing update thread request...");

        Long inputId = exchange.getIn().getHeader("id", Long.class);
        ThreadDTO inputThreadDTO = exchange.getIn().getBody(ThreadDTO.class);
        log.debug("Received input ID: {}", inputId);
        log.debug("Received input ThreadDTO: {}", inputThreadDTO);

        // add user id
        String userId = keycloakService.getUserId();
        inputThreadDTO.setUserId(userId);
        log.debug("Adding userId: {}", inputThreadDTO);

        ThreadModel threadToUpdateModel = ThreadDTOThreadModel.toModel(inputThreadDTO);

        ThreadModel updatedThreadModel = threadService.updateThread(inputId, threadToUpdateModel);
        log.debug("Updated thread: {}", updatedThreadModel);

        ThreadDTO updatedThreadDTO = ThreadDTOThreadModel.toDTO(updatedThreadModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Thread with name " + updatedThreadDTO.getName() + " updated successfully", updatedThreadDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);

        log.debug("Update thread request processed successfully.");
    }
}
