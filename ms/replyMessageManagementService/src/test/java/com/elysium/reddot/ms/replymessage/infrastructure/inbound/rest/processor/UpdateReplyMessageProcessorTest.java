package com.elysium.reddot.ms.replyreplyReplyMessage.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.replymessage.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageApplicationServiceImpl;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.UpdateReplyMessageProcessor;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessageProcessorMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateReplyMessageProcessorTest {

    private UpdateReplyMessageProcessor updateReplyMessageProcessor;

    @Mock
    private ReplyMessageApplicationServiceImpl replyReplyMessageService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        updateReplyMessageProcessor = new UpdateReplyMessageProcessor(replyReplyMessageService);
    }

    @Test
    @DisplayName("given valid replyReplyMessage when updateReplyMessage is called then replyReplyMessage updated successfully")
    void givenValidReplyMessage_whenUpdateReplyMessage_thenReplyMessageIsUpdatedSuccessfully() {
        // given
        Long replyReplyMessageId = 1L;
        ReplyMessageDTO updatedReplyMessageDTO = new ReplyMessageDTO("contentUpdated", 1L, "userId");
        ReplyMessageModel updatedReplyMessageModel = new ReplyMessageModel("contentUpdated", 1L, "userId");
        ReplyMessageDTO expectedReplyMessage = ReplyMessageProcessorMapper.toDTO(updatedReplyMessageModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Reply message with content " + updatedReplyMessageModel.getContent() + " updated successfully", expectedReplyMessage);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/replyReplyMessages/" + replyReplyMessageId);
        exchange.getIn().setHeader("id", replyReplyMessageId);
        exchange.getIn().setBody(updatedReplyMessageDTO);

        // mock
        when(replyReplyMessageService.updateReplyMessage(replyReplyMessageId, updatedReplyMessageModel)).thenReturn(updatedReplyMessageModel);

        // when
        updateReplyMessageProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }

}
