package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.replymessage.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageApplicationServiceImpl;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessageProcessorMapper;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.rabbitMQ.requester.MessageExistRequester;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateReplyMessageProcessor implements Processor {

    private final ReplyMessageApplicationServiceImpl replyMessageApplicationService;
    private final MessageExistRequester threadExistRequester;

    @Override
    public void process(Exchange exchange) {
        ReplyMessageDTO inputReplyMessageDTO = exchange.getIn().getBody(ReplyMessageDTO.class);
        ReplyMessageModel replyMessageModel = ReplyMessageProcessorMapper.toModel(inputReplyMessageDTO);

        threadExistRequester.verifyMessageIdExistsOrThrow(replyMessageModel.getParentMessageID());

        // add user id
        replyMessageModel.setUserId("1");

        createReplyMessageAndSetResponse(exchange, replyMessageModel);
    }

    private void createReplyMessageAndSetResponse(Exchange exchange, ReplyMessageModel replyMessageModel) {
        ReplyMessageModel createdReplyMessageModel = replyMessageApplicationService.createReplyMessage(replyMessageModel);

        ReplyMessageDTO createdReplyMessageDTO = ReplyMessageProcessorMapper.toDTO(createdReplyMessageModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "ReplyMessage with content " + createdReplyMessageModel.getContent() + " created successfully", createdReplyMessageDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }

}