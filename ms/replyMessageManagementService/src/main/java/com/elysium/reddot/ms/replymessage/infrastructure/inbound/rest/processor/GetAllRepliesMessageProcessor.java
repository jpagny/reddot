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

import java.util.List;

@Component
@AllArgsConstructor
public class GetAllRepliesMessageProcessor implements Processor {

    private final ReplyMessageApplicationServiceImpl replyReplyMessageService;

    @Override
    public void process(Exchange exchange) {
        List<ReplyMessageModel> listReplyMessagesModel = replyReplyMessageService.getAllRepliesMessage();

        List<ReplyMessageDTO> listReplyMessagesDTO = ReplyMessageProcessorMapper.toDTOList(listReplyMessagesModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "All replies message retrieved successfully", listReplyMessagesDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }
}
