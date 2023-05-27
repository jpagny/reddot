package com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.message;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.service.KeycloakService;
import com.elysium.reddot.ms.message.application.service.MessageApplicationServiceImpl;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.mapper.MessageProcessorMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;

@Component
@AllArgsConstructor
@Slf4j
public class UpdateMessageProcessor implements Processor {

    private final MessageApplicationServiceImpl messageService;
    private final KeycloakService keycloakService;
    private final ObjectMapper objectMapper;


    @Override
    public void process(Exchange exchange) throws AuthenticationException, JsonProcessingException {
        log.debug("Processing update message");
        Long inputId = exchange.getIn().getHeader("id", Long.class);
        String inputMessageJson = exchange.getIn().getBody(String.class);
        MessageDTO inputMessageDTO = objectMapper.readValue(inputMessageJson, MessageDTO.class);

        // add user id
        String userId = keycloakService.getUserId();
        log.debug("User id found :" + userId);
        inputMessageDTO.setUserId(userId);
        MessageModel messageToUpdateModel = MessageProcessorMapper.toModel(inputMessageDTO);
        log.debug("Processing update message");
        MessageModel updatedMessageModel = messageService.updateMessage(inputId, messageToUpdateModel);
        MessageDTO updatedMessageDTO = MessageProcessorMapper.toDTO(updatedMessageModel);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Message with content " + updatedMessageDTO.getContent() + " updated successfully", updatedMessageDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }
}
