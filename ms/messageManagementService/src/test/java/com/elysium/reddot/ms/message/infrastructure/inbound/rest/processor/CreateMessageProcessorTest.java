package com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.data.mapper.MessageDTOMessageModelMapper;
import com.elysium.reddot.ms.message.application.service.KeycloakService;
import com.elysium.reddot.ms.message.application.service.MessageApplicationServiceImpl;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.message.CreateMessageProcessor;
import com.elysium.reddot.ms.message.infrastructure.outbound.rabbitmq.requester.ThreadExistRequester;
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
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateMessageProcessorTest {

    private CreateMessageProcessor createMessageProcessor;

    @Mock
    private MessageApplicationServiceImpl messageApplicationService;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private ThreadExistRequester threadExistRequester;

    private ObjectMapper objectMapper;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        camelContext = new DefaultCamelContext();
        createMessageProcessor = new CreateMessageProcessor(messageApplicationService, keycloakService, threadExistRequester, objectMapper);

    }

    @Test
    @DisplayName("given valid message when createMessage is called then message created successfully")
    void givenValidMessage_whenCreateMessage_thenMessageCreatedSuccessfully() throws IOException, AuthenticationException {
        // given
        MessageDTO messageToCreateDTO = new MessageDTO("content", 1L, "userId");
        String messageToCreateDTOJson = objectMapper.writeValueAsString(messageToCreateDTO);

        MessageModel messageToCreateModel = new MessageModel("content", 1L, "userId");
        MessageModel createdMessageModel = new MessageModel(1L, "content", 1L, "Message description", LocalDateTime.now(), LocalDateTime.now());
        MessageDTO expectedMessage = MessageDTOMessageModelMapper.toDTO(createdMessageModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Message with content " + createdMessageModel.getContent() + " created successfully", expectedMessage);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/messages");
        exchange.getIn().setBody(messageToCreateDTOJson);

        // mock
        when(messageApplicationService.createMessage(messageToCreateModel)).thenReturn(createdMessageModel);
        doNothing().when(threadExistRequester).verifyThreadIdExistsOrThrow(1L);

        // when
        createMessageProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }


}
