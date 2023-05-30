package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.replymessage.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.application.service.KeycloakService;
import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageApplicationServiceImpl;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.replymessage.UpdateReplyMessageProcessor;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessageModelReplyMessageDTOMapper;
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
class UpdateReplyMessageProcessorTest {

    private UpdateReplyMessageProcessor updateReplyMessageProcessor;

    @Mock
    private ReplyMessageApplicationServiceImpl replyMessageService;

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
        updateReplyMessageProcessor = new UpdateReplyMessageProcessor(replyMessageService, keycloakService, objectMapper);
    }

    @Test
    @DisplayName("given valid replyReplyMessage when updateReplyMessage is called then replyReplyMessage updated successfully")
    void givenValidReplyMessage_whenUpdateReplyMessage_thenReplyMessageIsUpdatedSuccessfully() throws AuthenticationException, JsonProcessingException {
        // given
        Long replyMessageId = 1L;
        ReplyMessageDTO updatedMessageDTO = new ReplyMessageDTO("contentUpdated", 1L, "userId");
        String updatedMessageDTOJson = objectMapper.writeValueAsString(updatedMessageDTO);

        ReplyMessageModel updatedMessageModel = new ReplyMessageModel("contentUpdated", 1L, "userId");
        updatedMessageModel.setId(replyMessageId);
        ReplyMessageDTO expectedMessage = ReplyMessageModelReplyMessageDTOMapper.toDTO(updatedMessageModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Reply message updated successfully", expectedMessage);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/messages/" + replyMessageId);
        exchange.getIn().setHeader("id", replyMessageId);
        exchange.getIn().setBody(updatedMessageDTOJson);

        // mock
        when(keycloakService.getUserId()).thenReturn("userId");
        when(replyMessageService.updateReplyMessage(replyMessageId, updatedMessageModel)).thenReturn(updatedMessageModel);

        // when
        updateReplyMessageProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());

    }

}
