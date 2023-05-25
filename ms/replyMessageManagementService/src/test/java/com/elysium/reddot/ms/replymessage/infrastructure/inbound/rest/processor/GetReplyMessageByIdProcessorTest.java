package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.replymessage.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageApplicationServiceImpl;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.replymessage.GetReplyMessageByIdProcessor;
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
class GetReplyMessageByIdProcessorTest {

    private GetReplyMessageByIdProcessor getReplyMessageByIdProcessor;

    @Mock
    private ReplyMessageApplicationServiceImpl replyMessageApplicationService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        getReplyMessageByIdProcessor = new GetReplyMessageByIdProcessor(replyMessageApplicationService);
    }

    @Test
    @DisplayName("given replyMessage exists when getReplyMessageById is called then replyMessage retrieved")
    void givenReplyMessageExists_whenGetReplyMessageById_thenReplyMessageIsRetrieved() {
        // given
        Long id = 1L;
        ReplyMessageModel replyMessageModel = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageDTO expectedReplyMessage = ReplyMessageProcessorMapper.toDTO(replyMessageModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Reply message with id " + id + " retrieved successfully", expectedReplyMessage);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/replyMessages/" + id);
        exchange.getIn().setHeader("id", id);

        // mock
        when(replyMessageApplicationService.getReplyMessageById(id)).thenReturn(replyMessageModel);

        // when
        getReplyMessageByIdProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }

}
