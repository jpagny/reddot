package com.elysium.reddot.ms.message.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.message.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.message.application.service.MessageApplicationServiceImpl;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.data.exception.GlobalExceptionDTO;
import com.elysium.reddot.ms.message.infrastructure.exception.processor.GlobalExceptionHandler;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.*;
import com.elysium.reddot.ms.message.infrastructure.mapper.MessageProcessorMapper;
import com.elysium.reddot.ms.message.infrastructure.outbound.rabbitMQ.requester.ThreadExistRequester;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageRouteBuilderTest extends CamelTestSupport {

    @Mock
    private MessageApplicationServiceImpl messageService;

    @Mock
    private ThreadExistRequester threadExistRequester;

    @Override
    protected CamelContext createCamelContext() {
        return new DefaultCamelContext();
    }

    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper;
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        MessageProcessorHolder messageProcessorHolder = new MessageProcessorHolder(
                new GetAllMessagesProcessor(messageService),
                new GetMessageByIdProcessor(messageService),
                new CreateMessageProcessor(messageService, threadExistRequester),
                new UpdateMessageProcessor(messageService)
        );

        return new MessageRouteBuilder(globalExceptionHandler, messageProcessorHolder, objectMapper());
    }


    @Test
    @DisplayName("given messages exist when route getAllMessages is called then all messages retrieved")
    void givenMessagesExist_whenRouteGetAllMessages_thenAllMessagesRetrieved() {
        // given
        MessageModel message1Model = new MessageModel("content", 1L, "userId");
        MessageModel message2Model = new MessageModel("content2", 1L, "userId");
        List<MessageModel> messageListModel = Arrays.asList(message1Model, message2Model);
        List<MessageDTO> expectedListMessages = MessageProcessorMapper.toDTOList(messageListModel);

        Exchange exchange = new DefaultExchange(context);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All messages retrieved successfully", expectedListMessages);

        // mock
        when(messageService.getAllMessages()).thenReturn(messageListModel);

        // when
        Exchange responseExchange = template.send(MessageRouteConstants.GET_ALL_MESSAGES.getRouteName(), exchange);
        System.out.println("ICII 2: " + responseExchange.getIn().getBody().toString());
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }


    @Test
    @DisplayName("given existing message when route getMessageById is called with valid id then message returned")
    void givenExistingMessage_whenRouteGetMessageByIdWithValidId_thenMessageReturned() {
        // given
        Long messageId = 1L;
        MessageModel message = new MessageModel("content", 1L, "userId");
        MessageDTO expectedMessage = new MessageDTO("content", 1L, "userId");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", messageId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message with id 1 retrieved successfully", expectedMessage);

        // mock
        when(messageService.getMessageById(messageId)).thenReturn(message);

        // when
        Exchange result = template.send(MessageRouteConstants.GET_MESSAGE_BY_ID.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given non-existing message id when route getMessageById is called then throw ResourceNotFoundExceptionHandler")
    void givenNonExistingMessageId_whenRouteGetMessageById_thenThrowResourceNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;
        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The message with ID 99 does not exist.");

        // mock
        when(messageService.getMessageById(nonExistingId)).thenThrow(new ResourceNotFoundException("message", String.valueOf(nonExistingId)));

        // when
        Exchange result = template.send(MessageRouteConstants.GET_MESSAGE_BY_ID.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid message when route createMessage is called then message created")
    void givenValidMessage_whenRouteCreateMessage_thenMessageCreated() {
        // given
        MessageDTO inputMessageDTO = new MessageDTO("content", 1L, "userId");
        MessageModel inputMessageModel = new MessageModel("content", 1L, "userId");
        MessageModel createdMessageModel = new MessageModel(inputMessageModel.getContent(), inputMessageModel.getThreadId(), inputMessageModel.getUserId());
        MessageDTO expectedMessage = MessageProcessorMapper.toDTO(createdMessageModel);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputMessageDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Message with name " + expectedMessage.getContent() + " created successfully", expectedMessage);

        // mock
        when(messageService.createMessage(inputMessageModel)).thenReturn(createdMessageModel);

        // when
        Exchange responseExchange = template.send(MessageRouteConstants.CREATE_MESSAGE.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given message exists when route createMessage is called with creating same message then throws ResourceAlreadyExistExceptionHandler")
    void givenMessageExists_whenRouteCreateMessageWithCreatingSameMessage_thenThrowsResourceAlreadyExistExceptionHandler() {
        // given
        MessageDTO existingMessageDTO = new MessageDTO("content", 1L, "userId");
        MessageModel existingMessageModel = new MessageModel("content", 1L, "userId");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setBody(existingMessageDTO);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceAlreadyExistException",
                "The message with name 'name' already exists.");

        // mock
        when(messageService.createMessage(existingMessageModel)).thenThrow(new ResourceAlreadyExistException("message", "name", "name"));

        // when
        Exchange responseExchange = template.send(MessageRouteConstants.CREATE_MESSAGE.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid request when route updateMessage is called then message is updated")
    void givenValidRequest_whenRouteUpdateMessageIsCalled_thenMessageIsUpdated() {
        // given
        Long messageId = 1L;
        MessageDTO inputMessageDTO = new MessageDTO("content", 1L, "userId");
        MessageModel requestModel = new MessageModel("content", 1L, "userId");
        MessageModel updatedMessage = new MessageModel("content", 1L, "userId");
        MessageDTO expectedMessage = MessageProcessorMapper.toDTO(updatedMessage);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", messageId);
        exchange.getIn().setBody(inputMessageDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message with name " + updatedMessage.getContent() + " updated successfully", expectedMessage);

        // mock
        when(messageService.updateMessage(messageId, requestModel)).thenReturn(updatedMessage);

        // when
        Exchange responseExchange = template.send(MessageRouteConstants.UPDATE_MESSAGE.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given invalid request when route updateMessage is called then throws ResourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteUpdateMessage_thenThrowsResourceNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;
        MessageDTO inputRequestDTO = new MessageDTO("content", 1L, "userId");
        MessageModel request = new MessageModel("content", 1L, "userId");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(inputRequestDTO);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The message with ID 99 does not exist.");

        // mock
        when(messageService.updateMessage(nonExistingId, request)).thenThrow(new ResourceNotFoundException("message", String.valueOf(nonExistingId)));

        // when
        Exchange responseExchange = template.send(MessageRouteConstants.UPDATE_MESSAGE.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

}
