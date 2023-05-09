package com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.service.MessageApplicationServiceImpl;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.mapper.MessageProcessorMapper;
import com.elysium.reddot.ms.message.infrastructure.outbound.rabbitMQ.requester.ThreadExistRequester;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateMessageProcessor implements Processor {

    private final MessageApplicationServiceImpl messageApplicationService;
    private final ThreadExistRequester threadExistRequester;

    @Override
    public void process(Exchange exchange) {

        MessageDTO inputMessageDTO = exchange.getIn().getBody(MessageDTO.class);
        MessageModel messageModel = MessageProcessorMapper.toModel(inputMessageDTO);

        threadExistRequester.verifyThreadIdExistsOrThrow(messageModel.getThreadId());

        // add user id
        messageModel.setUserId("1");

        createMessageAndSetResponse(exchange, messageModel);
    }

    private void createMessageAndSetResponse(Exchange exchange, MessageModel messageModel) {
        MessageModel createdMessageModel = messageApplicationService.createMessage(messageModel);

        MessageDTO createdMessageDTO = MessageProcessorMapper.toDTO(createdMessageModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Message with content " + createdMessageModel.getContent() + " created successfully", createdMessageDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }

}