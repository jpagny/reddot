package com.elysium.reddot.ms.message.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.data.mapper.MessageDTOMessageModelMapper;
import com.elysium.reddot.ms.message.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.message.application.exception.type.ResourceBadValueException;
import com.elysium.reddot.ms.message.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.message.application.service.KeycloakService;
import com.elysium.reddot.ms.message.application.service.MessageApplicationServiceImpl;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.constant.MessageRouteEnum;
import com.elysium.reddot.ms.message.infrastructure.data.dto.GlobalExceptionDTO;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.exception.GlobalExceptionHandler;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.message.*;
import com.elysium.reddot.ms.message.infrastructure.outbound.rabbitmq.requester.ThreadExistRequester;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageRouteBuilderTest extends CamelTestSupport {

    @Mock
    private MessageApplicationServiceImpl messageService;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private ThreadExistRequester threadExistRequester;

    private ObjectMapper objectMapper;

    @Override
    protected CamelContext createCamelContext() {
        return new DefaultCamelContext();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        MessageProcessorHolder messageProcessorHolder = new MessageProcessorHolder(
                new GetAllMessagesProcessor(messageService),
                new GetMessageByIdProcessor(messageService),
                new CreateMessageProcessor(messageService, keycloakService, threadExistRequester, objectMapper),
                new UpdateMessageProcessor(messageService, keycloakService, objectMapper)
        );

        return new MessageRouteBuilder(globalExceptionHandler, messageProcessorHolder, objectMapper);
    }

    @Test
    @DisplayName("given messages exist when route getAllMessages is called then all messages retrieved")
    void givenMessagesExist_whenRouteGetAllMessages_thenAllMessagesRetrieved() throws IOException {
        // given
        MessageModel message1Model = new MessageModel("content", 1L, "userId");
        MessageModel message2Model = new MessageModel("content2", 1L, "userId");
        List<MessageModel> messageListModel = Arrays.asList(message1Model, message2Model);
        List<MessageDTO> expectedListMessages = MessageDTOMessageModelMapper.toDTOList(messageListModel);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All messages retrieved successfully", expectedListMessages);

        // mock
        when(messageService.getAllMessages()).thenReturn(messageListModel);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.GET_ALL_MESSAGES.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }


    @Test
    @DisplayName("given existing message when route getMessageById is called with valid id then message returned")
    void givenExistingMessage_whenRouteGetMessageByIdWithValidId_thenMessageReturned() throws IOException {
        // given
        Long messageId = 1L;
        MessageModel message = new MessageModel("content", 1L, "userId");
        MessageDTO expectedMessage = new MessageDTO("content", 1L, "userId");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", messageId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message with id 1 retrieved successfully", expectedMessage);

        // mock
        when(messageService.getMessageById(messageId)).thenReturn(message);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.GET_MESSAGE_BY_ID.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given non-existing message id when route getMessageById is called then throw ResourceNotFoundExceptionHandler")
    void givenNonExistingMessageId_whenRouteGetMessageById_thenThrowResourceNotFoundExceptionHandler() throws JsonProcessingException {
        // given
        Long nonExistingId = 99L;

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The message with ID 99 does not exist.");

        // mock
        when(messageService.getMessageById(nonExistingId)).thenThrow(new ResourceNotFoundException("message", String.valueOf(nonExistingId)));

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.GET_MESSAGE_BY_ID.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        GlobalExceptionDTO actualResponse = objectMapper.readValue(responseJson, GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid message when route createMessage is called then message created")
    void givenValidMessage_whenRouteCreateMessage_thenMessageCreated() throws IOException {
        // given
        MessageDTO inputMessageDTO = new MessageDTO("content", 1L, "userId");
        String inputMessageDTOJson = objectMapper.writeValueAsString(inputMessageDTO);
        MessageModel inputMessageModel = new MessageModel("content", 1L, "userId");
        MessageModel createdMessageModel = new MessageModel(inputMessageModel.getContent(), inputMessageModel.getThreadId(), inputMessageModel.getUserId());
        MessageDTO expectedMessage = MessageDTOMessageModelMapper.toDTO(createdMessageModel);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputMessageDTOJson);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Message with content " + expectedMessage.getContent() + " created successfully", expectedMessage);

        // mock
        doNothing().when(threadExistRequester).verifyThreadIdExistsOrThrow(1L);
        when(messageService.createMessage(inputMessageModel)).thenReturn(createdMessageModel);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.CREATE_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given message exists when route createMessage is called with creating same message then throws ResourceAlreadyExistExceptionHandler")
    void givenMessageExists_whenRouteCreateMessageWithCreatingSameMessage_thenThrowsResourceAlreadyExistExceptionHandler() throws JsonProcessingException {
        // given
        MessageDTO existingMessageDTO = new MessageDTO("content", 1L, "userId");
        String existingMessageDTOJson = objectMapper.writeValueAsString(existingMessageDTO);
        MessageModel existingMessageModel = new MessageModel("content", 1L, "userId");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setBody(existingMessageDTOJson);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceAlreadyExistException",
                "The message with name 'name' already exists with board id 1.");

        // mock
        when(messageService.createMessage(existingMessageModel)).thenThrow(new ResourceAlreadyExistException("message", "name", "name", 1L));

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.CREATE_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        GlobalExceptionDTO actualResponse = objectMapper.readValue(responseJson, GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given invalid message when route createMessage is called then throw ResourceBadValueException")
    void givenInvalidMessage_whenRouteCreateMessage_thenMessageCreated() throws IOException {
        // given
        MessageDTO inputMessageDTO = new MessageDTO(null, 1L, "userId");
        String inputMessageDTOJson = objectMapper.writeValueAsString(inputMessageDTO);
        MessageModel messageModelWithBadValue = MessageDTOMessageModelMapper.toModel(inputMessageDTO);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputMessageDTOJson);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceBadValueException",
                "The message has bad value : content.");

        // mock
        when(messageService.createMessage(messageModelWithBadValue)).thenThrow(new ResourceBadValueException("message", "content"));
        doNothing().when(threadExistRequester).verifyThreadIdExistsOrThrow(1L);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.CREATE_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        GlobalExceptionDTO actualResponse = objectMapper.readValue(responseJson, GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }


    @Test
    @DisplayName("given valid request when route updateMessage is called then message is updated")
    void givenValidRequest_whenRouteUpdateMessageIsCalled_thenMessageIsUpdated() throws IOException {
        // given
        Long messageId = 1L;
        MessageDTO inputMessageDTO = new MessageDTO("content", 1L, "userId");
        String inputMessageDTOJson = objectMapper.writeValueAsString(inputMessageDTO);
        MessageModel requestModel = new MessageModel("content", 1L, "userId");
        MessageModel updatedMessage = new MessageModel("content", 1L, "userId");
        MessageDTO expectedMessage = MessageDTOMessageModelMapper.toDTO(updatedMessage);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", messageId);
        exchange.getIn().setBody(inputMessageDTOJson);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message with content " + updatedMessage.getContent() + " updated successfully", expectedMessage);

        // mock
        when(messageService.updateMessage(messageId, requestModel)).thenReturn(updatedMessage);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.UPDATE_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given invalid request when route updateMessage is called then throws ResourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteUpdateMessage_thenThrowsResourceNotFoundExceptionHandler() throws JsonProcessingException {
        // given
        Long nonExistingId = 99L;
        MessageDTO inputRequestDTO = new MessageDTO("content", 1L, "userId");
        String inputMessageDTOJson = objectMapper.writeValueAsString(inputRequestDTO);
        MessageModel request = new MessageModel("content", 1L, "userId");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(inputMessageDTOJson);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The message with ID 99 does not exist.");

        // mock
        when(messageService.updateMessage(nonExistingId, request)).thenThrow(new ResourceNotFoundException("message", String.valueOf(nonExistingId)));

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.UPDATE_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        GlobalExceptionDTO actualResponse = objectMapper.readValue(responseJson, GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

}
