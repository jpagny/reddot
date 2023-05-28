package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.replymessage;

import com.elysium.reddot.ms.replymessage.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.application.service.KeycloakService;
import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageApplicationServiceImpl;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessageProcessorMapper;
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


    @Override
    public void process(Exchange exchange) throws JsonProcessingException, AuthenticationException {
        Long inputId = exchange.getIn().getHeader("id", Long.class);
        String inputMessageJson = exchange.getIn().getBody(String.class);
        ReplyMessageDTO inputMessageDTO = objectMapper.readValue(inputMessageJson, ReplyMessageDTO.class);
        inputMessageDTO.setId(inputId);

        // add user id
        String userId = keycloakService.getUserId();
        inputMessageDTO.setUserId(userId);

        ReplyMessageModel replyMessageToUpdateModel = ReplyMessageProcessorMapper.toModel(inputMessageDTO);

        ReplyMessageModel updatedReplyMessageModel = replyMessageService.updateReplyMessage(inputId, replyMessageToUpdateModel);

        ReplyMessageDTO updatedReplyMessageDTO = ReplyMessageProcessorMapper.toDTO(updatedReplyMessageModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Reply message updated successfully", updatedReplyMessageDTO);
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }
}
