package com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.service.MessageApplicationServiceImpl;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.message.UpdateMessageProcessor;
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
class UpdateMessageProcessorTest {

    private UpdateMessageProcessor updateMessageProcessor;

    @Mock
    private MessageApplicationServiceImpl messageService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        updateMessageProcessor = new UpdateMessageProcessor(messageService);
    }

    @Test
    @DisplayName("given valid message when updateMessage is called then message updated successfully")
    void givenValidMessage_whenUpdateMessage_thenMessageIsUpdatedSuccessfully() {
        // given
        Long messageId = 1L;
        MessageDTO updatedMessageDTO = new MessageDTO("contentUpdated", 1L, "userId");
        MessageModel updatedMessageModel = new MessageModel("contentUpdated", 1L, "userId");
        MessageDTO expectedMessage = MessageProcessorMapper.toDTO(updatedMessageModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message with content " + updatedMessageModel.getContent() + " updated successfully", expectedMessage);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/messages/" + messageId);
        exchange.getIn().setHeader("id", messageId);
        exchange.getIn().setBody(updatedMessageDTO);

        // mock
        when(messageService.updateMessage(messageId, updatedMessageModel)).thenReturn(updatedMessageModel);

        // when
        updateMessageProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }

}
