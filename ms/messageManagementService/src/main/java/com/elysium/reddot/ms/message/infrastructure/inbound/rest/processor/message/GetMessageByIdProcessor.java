package com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.message;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.service.MessageApplicationServiceImpl;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.application.data.mapper.MessageDTOMessageModelMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Processor for retrieving a message by its ID.
 */
@Component
@AllArgsConstructor
@Slf4j
public class GetMessageByIdProcessor implements Processor {

    private final MessageApplicationServiceImpl messageService;

    /**
     * Processes the incoming exchange to retrieve a message by its ID.
     *
     * @param exchange the Camel Exchange object
     */
    @Override
    public void process(Exchange exchange) {
        log.debug("Processing GetMessageByIdProcessor...");

        Long inputId = Long.parseLong(exchange.getIn().getHeader("id").toString());
        log.debug("Retrieving message with ID: {}", inputId);

        MessageModel messageModel = messageService.getMessageById(inputId);
        log.debug("Retrieved message with ID: {}", inputId);

        MessageDTO messageDTO = MessageDTOMessageModelMapper.toDTO(messageModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message with id " + inputId + " retrieved successfully", messageDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);

        log.debug("GetMessageByIdProcessor processing completed.");
    }

}