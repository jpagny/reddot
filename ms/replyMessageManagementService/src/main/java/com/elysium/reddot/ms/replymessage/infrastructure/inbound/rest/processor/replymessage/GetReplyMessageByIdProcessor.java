package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.replymessage;

import com.elysium.reddot.ms.replymessage.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageApplicationServiceImpl;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessageModelReplyMessageDTOMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class GetReplyMessageByIdProcessor implements Processor {

    private final ReplyMessageApplicationServiceImpl replyMessageService;

    /**
     * Processes the retrieval of a reply message by ID.
     *
     * @param exchange the Camel exchange object
     */
    @Override
    public void process(Exchange exchange) {
        log.debug("Processing get reply message by ID...");

        Long inputId = Long.parseLong(exchange.getIn().getHeader("id").toString());
        log.debug("Input ID: {}", inputId);

        ReplyMessageModel replyMessageModel = replyMessageService.getReplyMessageById(inputId);
        log.debug("Retrieved reply message by ID: {}", replyMessageModel);

        ReplyMessageDTO replyMessageDTO = ReplyMessageModelReplyMessageDTOMapper.toDTO(replyMessageModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "Reply message with id " + inputId + " retrieved successfully", replyMessageDTO);
        log.debug("Created API response DTO: {}", apiResponseDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);

        log.debug("Set get reply message by ID response: {}", apiResponseDTO);
    }

}