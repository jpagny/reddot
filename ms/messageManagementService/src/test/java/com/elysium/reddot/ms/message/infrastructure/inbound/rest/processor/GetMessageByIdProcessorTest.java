package com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.service.MessageApplicationServiceImpl;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.message.GetMessageByIdProcessor;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMessageByIdProcessorTest {

    private GetMessageByIdProcessor getMessageByIdProcessor;

    @Mock
    private MessageApplicationServiceImpl messageService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        getMessageByIdProcessor = new GetMessageByIdProcessor(messageService);
    }

    @Test
    @DisplayName("given message exists when getMessageById is called then message retrieved")
    void givenMessageExists_whenGetMessageById_thenMessageIsRetrieved() {
        // given
        Long id = 1L;
        MessageModel messageModel = new MessageModel("content", 1L, "userId");
        MessageDTO expectedMessage = MessageProcessorMapper.toDTO(messageModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message with id " + id + " retrieved successfully", expectedMessage);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/messages/" + id);
        exchange.getIn().setHeader("id", id);

        // mock
        when(messageService.getMessageById(id)).thenReturn(messageModel);

        // when
        getMessageByIdProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }

}
