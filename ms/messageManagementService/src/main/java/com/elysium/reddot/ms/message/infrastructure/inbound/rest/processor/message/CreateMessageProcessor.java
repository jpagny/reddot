package com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.message;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.service.KeycloakService;
import com.elysium.reddot.ms.message.application.service.MessageApplicationServiceImpl;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.application.data.mapper.MessageDTOMessageModelMapper;
import com.elysium.reddot.ms.message.infrastructure.outbound.rabbitmq.requester.ThreadExistRequester;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.io.IOException;

/**
 * Processor for creating a new message.
 */
@Component
@AllArgsConstructor
@Slf4j
public class CreateMessageProcessor implements Processor {

    private final MessageApplicationServiceImpl messageApplicationService;
    private final KeycloakService keycloakService;
    private final ThreadExistRequester threadExistRequester;
    private final ObjectMapper objectMapper;

    /**
     * Processes the incoming exchange to create a new message.
     *
     * @param exchange the Camel Exchange object
     * @throws IOException             if there is an error reading the input message JSON
     * @throws AuthenticationException if there is an authentication error
     */
    @Override
    public void process(Exchange exchange) throws IOException, AuthenticationException {
        log.debug("Processing CreateMessageProcessor...");

        String inputMessageJson = exchange.getIn().getBody(String.class);
        MessageDTO inputMessageDTO = objectMapper.readValue(inputMessageJson, MessageDTO.class);
        log.debug("Received input message JSON: {}", inputMessageJson);


        // add user id
        String userId = keycloakService.getUserId();
        inputMessageDTO.setUserId(userId);
        log.debug("Adding userId: {}", inputMessageDTO);

        MessageModel messageModel = MessageDTOMessageModelMapper.toModel(inputMessageDTO);

        log.debug("Verifying existence of board ID: {}", messageModel.getThreadId());
        threadExistRequester.verifyThreadIdExistsOrThrow(messageModel.getThreadId());

        createMessageAndSetResponse(exchange, messageModel);
    }

    private void createMessageAndSetResponse(Exchange exchange, MessageModel messageModel) {
        log.debug("Creating message with content '{}'", messageModel.getContent());

        MessageModel createdMessageModel = messageApplicationService.createMessage(messageModel);
        log.info("Message with ID '{}' created successfully", createdMessageModel.getId());

        MessageDTO createdMessageDTO = MessageDTOMessageModelMapper.toDTO(createdMessageModel);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Message with content " + createdMessageModel.getContent() + " created successfully", createdMessageDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
        exchange.getMessage().setBody(apiResponseDTO);

        log.debug("Set HTTP response code to {}", HttpStatus.CREATED.value());
        log.debug("Set response body to {}", apiResponseDTO);
    }


}