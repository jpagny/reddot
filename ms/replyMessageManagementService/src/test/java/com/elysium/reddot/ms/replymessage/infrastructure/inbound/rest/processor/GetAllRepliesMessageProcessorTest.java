package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.replymessage.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageApplicationServiceImpl;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.replymessage.GetAllRepliesMessageProcessor;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessageModelReplyMessageDTOMapper;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllRepliesMessageProcessorTest {

    private GetAllRepliesMessageProcessor getAllRepliesMessageProcessor;

    @Mock
    private ReplyMessageApplicationServiceImpl replyMessageApplicationService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        getAllRepliesMessageProcessor = new GetAllRepliesMessageProcessor(replyMessageApplicationService);
    }

    @Test
    @DisplayName("given replies message exist when get allRepliesMessage is called then all replies message retrieved")
    void givenRepliesMessageExist_whenGetAllRepliesMessage_thenAllRepliesMessageAreRetrieved() {
        // given
        ReplyMessageModel message1Model = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageModel message2Model = new ReplyMessageModel("content2", 1L, "userId");
        List<ReplyMessageModel> messageListModel = Arrays.asList(message1Model, message2Model);
        List<ReplyMessageDTO> expectedListMessages = ReplyMessageModelReplyMessageDTOMapper.toDTOList(messageListModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All replies message retrieved successfully", expectedListMessages);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/repliesMessage");

        // mock
        when(replyMessageApplicationService.getAllRepliesMessage()).thenReturn(messageListModel);

        // when
        getAllRepliesMessageProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }


}
