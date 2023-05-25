package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.replymessage;

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
public class GetReplyMessageByIdProcessor implements Processor {

    private final ReplyMessageApplicationServiceImpl replyMessageService;

    @Override
    public void process(Exchange exchange) {
        Long inputId = Long.parseLong(exchange.getIn().getHeader("id").toString());

        ReplyMessageModel replyMessageModel = replyMessageService.getReplyMessageById(inputId);

        ReplyMessageDTO replyMessageDTO = ReplyMessageProcessorMapper.toDTO(replyMessageModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "Reply message with id " + inputId + " retrieved successfully", replyMessageDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }

}