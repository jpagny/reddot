package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.replymessage;

import com.elysium.reddot.ms.replymessage.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageApplicationServiceImpl;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessageProcessorMapper;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.rabbitmq.requester.MessageExistRequester;
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
public class CreateReplyMessageProcessor implements Processor {

    private final ReplyMessageApplicationServiceImpl replyMessageApplicationService;
    private final MessageExistRequester messageExistRequester;

    @Override
    public void process(Exchange exchange) throws IOException {
        ReplyMessageDTO inputReplyMessageDTO = exchange.getIn().getBody(ReplyMessageDTO.class);
        ReplyMessageModel replyMessageModel = ReplyMessageProcessorMapper.toModel(inputReplyMessageDTO);

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