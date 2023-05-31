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

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class GetAllRepliesMessageProcessor implements Processor {

    private final ReplyMessageApplicationServiceImpl replyReplyMessageService;

    /**
     * Processes the retrieval of all reply messages.
     *
     * @param exchange the Camel exchange object
     */
    @Override
    public void process(Exchange exchange) {
        log.debug("Processing get all reply messages...");

        List<ReplyMessageModel> listReplyMessagesModel = replyReplyMessageService.getAllRepliesMessage();
        log.debug("Retrieved {} reply messages", listReplyMessagesModel.size());

        List<ReplyMessageDTO> listReplyMessagesDTO = ReplyMessageModelReplyMessageDTOMapper.toDTOList(listReplyMessagesModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "All replies message retrieved successfully", listReplyMessagesDTO);
        log.debug("Created API response DTO: {}", apiResponseDTO);


        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);

        log.debug("Set get all reply messages response: {}", apiResponseDTO);
    }
}
