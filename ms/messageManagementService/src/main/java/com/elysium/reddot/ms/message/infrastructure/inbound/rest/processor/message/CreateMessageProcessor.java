package com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.message;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.service.MessageApplicationServiceImpl;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.mapper.MessageProcessorMapper;
import com.elysium.reddot.ms.message.infrastructure.outbound.rabbitmq.requester.ThreadExistRequester;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
@Slf4j
public class CreateMessageProcessor implements Processor {

    private final MessageApplicationServiceImpl messageApplicationService;
    private final ThreadExistRequester threadExistRequester;

    @Override
    public void process(Exchange exchange) throws IOException {
        MessageDTO inputMessageDTO = exchange.getIn().getBody(MessageDTO.class);
        MessageModel messageModel = MessageProcessorMapper.toModel(inputMessageDTO);

        threadExistRequester.verifyThreadIdExistsOrThrow(messageModel.getThreadId());

        createMessageAndSetResponse(exchange, messageModel);
    }

    private void createMessageAndSetResponse(Exchange exchange, MessageModel messageModel) {
        log.debug("ICI 222");
        MessageModel createdMessageModel = messageApplicationService.createMessage(messageModel);
        log.debug("ICI 333");
        MessageDTO createdMessageDTO = MessageProcessorMapper.toDTO(createdMessageModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Message with content " + createdMessageModel.getContent() + " created successfully", createdMessageDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }

}