package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.replymessage.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageApplicationServiceImpl;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessageProcessorMapper;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdateReplyMessageProcessor implements Processor {

    private final ReplyMessageApplicationServiceImpl replyReplyMessageService;

    @Override
    public void process(Exchange exchange) {
        /*
        Long inputId = exchange.getIn().getHeader("id", Long.class);
        ReplyMessageDTO inputReplyMessageDTO = exchange.getIn().getBody(ReplyMessageDTO.class);
        ReplyMessageModel replyReplyMessageToUpdateModel = com.elysium.reddot.ms.replyreplyMessage.infrastructure.mapper.ReplyMessageProcessorMapper.toModel(inputReplyMessageDTO);

        ReplyMessageModel updatedReplyMessageModel = replyReplyMessageService.updateReplyMessage(inputId, replyReplyMessageToUpdateModel);

        ReplyMessageDTO updatedReplyMessageDTO = ReplyMessageProcessorMapper.toDTO(updatedReplyMessageModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "ReplyMessage with content " + updatedReplyMessageDTO.getContent() + " updated successfully", updatedReplyMessageDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);
        
         */
    }
}
