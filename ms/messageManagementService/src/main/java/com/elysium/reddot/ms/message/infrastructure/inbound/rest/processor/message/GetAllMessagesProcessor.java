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

import java.util.List;

/**
 * Processor for retrieving all messages.
 */
@Component
@AllArgsConstructor
@Slf4j
public class GetAllMessagesProcessor implements Processor {

    private final MessageApplicationServiceImpl messageService;

    /**
     * Processes the incoming exchange to retrieve all messages.
     *
     * @param exchange the Camel Exchange object
     */
    @Override
    public void process(Exchange exchange) {
        List<MessageModel> listMessagesModel = messageService.getAllMessages();
        List<MessageDTO> listMessagesDTO = MessageDTOMessageModelMapper.toDTOList(listMessagesModel);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "All messages retrieved successfully", listMessagesDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }
}
