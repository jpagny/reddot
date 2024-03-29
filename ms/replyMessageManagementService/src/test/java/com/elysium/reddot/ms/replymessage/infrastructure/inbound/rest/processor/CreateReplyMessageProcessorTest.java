package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.replymessage.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.application.service.KeycloakService;
import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageApplicationServiceImpl;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.replymessage.CreateReplyMessageProcessor;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessageModelReplyMessageDTOMapper;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.rabbitmq.requester.MessageExistRequester;
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
import org.mockito.InjectMocks;
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
class CreateReplyMessageProcessorTest {

    @InjectMocks
    private CreateReplyMessageProcessor createReplyMessageProcessor;

    @Mock
    private ReplyMessageApplicationServiceImpl replyMessageService;

    @Mock
    private KeycloakService keycloakService;

    private ObjectMapper objectMapper;

    @Mock
    private MessageExistRequester messageExistRequester;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        camelContext = new DefaultCamelContext();
        createReplyMessageProcessor = new CreateReplyMessageProcessor(replyMessageService, keycloakService, messageExistRequester, objectMapper);

    }

    @Test
    @DisplayName("given valid replyMessage when createReplyMessage is called then replyMessage created successfully")
    void givenValidReplyMessage_whenCreateReplyMessage_thenReplyMessageCreatedSuccessfully() throws IOException, AuthenticationException {
        // given
        ReplyMessageDTO messageToCreateDTO = new ReplyMessageDTO("content", 1L, "userId");
        String updatedMessageDTOJson = objectMapper.writeValueAsString(messageToCreateDTO);

        ReplyMessageModel messageToCreateModel = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageModel createdMessageModel = new ReplyMessageModel(1L, "content", 1L, "Message description", LocalDateTime.now(), LocalDateTime.now());
        ReplyMessageDTO expectedMessage = ReplyMessageModelReplyMessageDTOMapper.toDTO(createdMessageModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Reply message with content " + createdMessageModel.getContent() + " created successfully", expectedMessage);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/repliesMessage");
        exchange.getIn().setBody(updatedMessageDTOJson);

        // mock
        when(replyMessageService.createReplyMessage(messageToCreateModel)).thenReturn(createdMessageModel);
        doNothing().when(messageExistRequester).verifyMessageIdExistsOrThrow(1L);

        // when
        createReplyMessageProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }


}
