package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.replymessage;

import com.elysium.reddot.ms.replymessage.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.application.service.KeycloakService;
import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageApplicationServiceImpl;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessageModelReplyMessageDTOMapper;
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
public class UpdateReplyMessageProcessor implements Processor {

    private final ReplyMessageApplicationServiceImpl replyMessageService;
    private final KeycloakService keycloakService;
    private final ObjectMapper objectMapper;

    /**
     * Processes the update of a reply message.
     *
     * @param exchange the Camel exchange object
     * @throws JsonProcessingException if there is an error in processing JSON data
     * @throws AuthenticationException if there is an authentication error
     */
    @Override
    public void process(Exchange exchange) throws JsonProcessingException, AuthenticationException {
        log.debug("Processing update reply message...");

        Long inputId = exchange.getIn().getHeader("id", Long.class);
        log.debug("Input ID: {}", inputId);

        String inputMessageJson = exchange.getIn().getBody(String.class);
        log.debug("Input message JSON: {}", inputMessageJson);

        ReplyMessageDTO inputMessageDTO = objectMapper.readValue(inputMessageJson, ReplyMessageDTO.class);
        inputMessageDTO.setId(inputId);

        // add user id
        String userId = keycloakService.getUserId();
        inputMessageDTO.setUserId(userId);
        log.debug("User ID: {}", userId);


        ReplyMessageModel replyMessageToUpdateModel = ReplyMessageModelReplyMessageDTOMapper.toModel(inputMessageDTO);
        ReplyMessageModel updatedReplyMessageModel = replyMessageService.updateReplyMessage(inputId, replyMessageToUpdateModel);
        log.debug("Updated reply message model: {}", updatedReplyMessageModel);


        ReplyMessageDTO updatedReplyMessageDTO = ReplyMessageModelReplyMessageDTOMapper.toDTO(updatedReplyMessageModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Reply message updated successfully", updatedReplyMessageDTO);
        log.debug("Created API response DTO: {}", apiResponseDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);

        log.debug("Set update reply message response: {}", apiResponseDTO);
    }
}
