package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.replymessage;

import com.elysium.reddot.ms.replymessage.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.application.service.KeycloakService;
import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageApplicationServiceImpl;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessageProcessorMapper;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.rabbitmq.requester.MessageExistRequester;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.io.IOException;

@Component
@AllArgsConstructor
@Slf4j
public class CreateReplyMessageProcessor implements Processor {

    private final ReplyMessageApplicationServiceImpl replyMessageApplicationService;
    private final KeycloakService keycloakService;
    private final MessageExistRequester messageExistRequester;
    private final ObjectMapper objectMapper;

    @Override
    public void process(Exchange exchange) throws IOException, AuthenticationException {
        String inputMessageJson = exchange.getIn().getBody(String.class);
        ReplyMessageDTO inputMessageDTO = objectMapper.readValue(inputMessageJson, ReplyMessageDTO.class);

        // add user id
        String userId = keycloakService.getUserId();
        inputMessageDTO.setUserId(userId);

        ReplyMessageModel replyMessageModel = ReplyMessageProcessorMapper.toModel(inputMessageDTO);

        // verify parent message id exists
        messageExistRequester.verifyMessageIdExistsOrThrow(replyMessageModel.getParentMessageID());

        createReplyMessageAndSetResponse(exchange, replyMessageModel);
    }

    private void createReplyMessageAndSetResponse(Exchange exchange, ReplyMessageModel replyMessageModel) {
        ReplyMessageModel createdReplyMessageModel = replyMessageApplicationService.createReplyMessage(replyMessageModel);

        ReplyMessageDTO createdReplyMessageDTO = ReplyMessageProcessorMapper.toDTO(createdReplyMessageModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Reply message with content " + createdReplyMessageModel.getContent() + " created successfully", createdReplyMessageDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }

}