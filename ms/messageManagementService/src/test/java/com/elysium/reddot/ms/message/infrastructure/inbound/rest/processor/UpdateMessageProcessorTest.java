package com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.data.mapper.MessageDTOMessageModelMapper;
import com.elysium.reddot.ms.message.application.service.KeycloakService;
import com.elysium.reddot.ms.message.application.service.MessageApplicationServiceImpl;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.message.UpdateMessageProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import javax.naming.AuthenticationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateMessageProcessorTest {

    private UpdateMessageProcessor updateMessageProcessor;

    @Mock
    private MessageApplicationServiceImpl messageService;

    @Mock
    private KeycloakService keycloakService;

    private ObjectMapper objectMapper;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        camelContext = new DefaultCamelContext();
        updateMessageProcessor = new UpdateMessageProcessor(messageService, keycloakService, objectMapper);
    }

    @Test
    @DisplayName("given valid message when updateMessage is called then message updated successfully")
    void givenValidMessage_whenUpdateMessage_thenMessageIsUpdatedSuccessfully() throws AuthenticationException, JsonProcessingException {
        // given
        Long messageId = 1L;
        MessageDTO updatedMessageDTO = new MessageDTO("contentUpdated", 1L, "userId");
        String updatedMessageDTOJson = objectMapper.writeValueAsString(updatedMessageDTO);

        MessageModel updatedMessageModel = new MessageModel("contentUpdated", 1L, "userId");
        MessageDTO expectedMessage = MessageDTOMessageModelMapper.toDTO(updatedMessageModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message with content " + updatedMessageModel.getContent() + " updated successfully", expectedMessage);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/messages/" + messageId);
        exchange.getIn().setHeader("id", messageId);
        exchange.getIn().setBody(updatedMessageDTOJson);

        // mock
        when(keycloakService.getUserId()).thenReturn("userId");
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
