package com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.service.MessageApplicationServiceImpl;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.message.GetAllMessagesProcessor;
import com.elysium.reddot.ms.message.infrastructure.mapper.MessageProcessorMapper;
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
class GetAllMessagesProcessorTest {

    private GetAllMessagesProcessor getAllMessagesProcessor;

    @Mock
    private MessageApplicationServiceImpl messageService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        getAllMessagesProcessor = new GetAllMessagesProcessor(messageService);
    }

    @Test
    @DisplayName("given messages exist when get allMessages is called then all messages retrieved")
    void givenMessagesExist_whenGetAllMessages_thenAllMessagesAreRetrieved() {
        // given
        MessageModel message1Model = new MessageModel("content", 1L, "userId");
        MessageModel message2Model = new MessageModel("content2", 1L, "userId");
        List<MessageModel> messageListModel = Arrays.asList(message1Model, message2Model);
        List<MessageDTO> expectedListMessages = MessageProcessorMapper.toDTOList(messageListModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All messages retrieved successfully", expectedListMessages);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/messages");

        // mock
        when(messageService.getAllMessages()).thenReturn(messageListModel);

        // when
        getAllMessagesProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }


}
