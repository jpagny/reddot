package com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.service.MessageApplicationServiceImpl;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.mapper.MessageProcessorMapper;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdateMessageProcessor implements Processor {

    private final MessageApplicationServiceImpl messageService;

    @Override
    public void process(Exchange exchange) {
        /*
        Long inputId = exchange.getIn().getHeader("id", Long.class);
        MessageDTO inputMessageDTO = exchange.getIn().getBody(MessageDTO.class);
        MessageModel messageToUpdateModel = MessageProcessorMapper.toModel(inputMessageDTO);

        MessageModel updatedMessageModel = messageService.updateMessage(inputId, messageToUpdateModel);

        MessageDTO updatedMessageDTO = MessageProcessorMapper.toDTO(updatedMessageModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Message with content " + updatedMessageDTO.getContent() + " updated successfully", updatedMessageDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);

         */
    }
}
