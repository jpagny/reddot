package com.elysium.reddot.ms.message.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.message.application.exception.type.ResourceBadValueException;
import com.elysium.reddot.ms.message.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.message.application.service.KeycloakService;
import com.elysium.reddot.ms.message.application.service.MessageApplicationServiceImpl;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.data.dto.GlobalExceptionDTO;
import com.elysium.reddot.ms.message.infrastructure.constant.MessageRouteEnum;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.exception.GlobalExceptionHandler;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.keycloak.CheckTokenProcessor;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.keycloak.KeycloakProcessorHolder;
import com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor.message.*;
import com.elysium.reddot.ms.message.infrastructure.mapper.MessageProcessorMapper;
import com.elysium.reddot.ms.message.infrastructure.outbound.rabbitmq.requester.ThreadExistRequester;
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
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
                new CreateMessageProcessor(messageService, threadExistRequester),
                new UpdateMessageProcessor(messageService)
        );

        KeycloakProcessorHolder keycloakProcessorHolder = new KeycloakProcessorHolder(
                new CheckTokenProcessor(keycloakService)
        );

        return new MessageRouteBuilder(globalExceptionHandler, messageProcessorHolder, keycloakProcessorHolder, objectMapper);
    }

    @Test
    @DisplayName("given the token is inactive, when getAllMessages route is processed, then TokenNotActiveException is returned")
    void givenInactiveToken_whenRouteGetAllMessages_thenTokenNotActiveExceptionReturned() throws Exception {
        // given
        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":false}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("TokenNotActiveException",
                "Your token is inactive.");

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.GET_ALL_MESSAGES.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("Given an active token without user role, when getAllMessages route is processed, then HasNotRoleException is returned")
    void givenActiveTokenWithoutUserRole_whenRouteGetAllMessages_thenHasNotRoleExceptionReturned() throws Exception {
        // given
        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"bidon\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("HasNotRoleException",
                "Having role user is required.");

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.GET_ALL_MESSAGES.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }


    @Test
    @DisplayName("given messages exist when route getAllMessages is called then all messages retrieved")
    void givenMessagesExist_whenRouteGetAllMessages_thenAllMessagesRetrieved() throws IOException, URISyntaxException {
        // given
        MessageModel message1Model = new MessageModel("content", 1L, "userId");
        MessageModel message2Model = new MessageModel("content2", 1L, "userId");
        List<MessageModel> messageListModel = Arrays.asList(message1Model, message2Model);
        List<MessageDTO> expectedListMessages = MessageProcessorMapper.toDTOList(messageListModel);

        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All messages retrieved successfully", expectedListMessages);

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
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
    void givenExistingMessage_whenRouteGetMessageByIdWithValidId_thenMessageReturned() throws IOException, URISyntaxException {
        // given
        Long messageId = 1L;
        MessageModel message = new MessageModel("content", 1L, "userId");
        MessageDTO expectedMessage = new MessageDTO("content", 1L, "userId");

        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", messageId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message with id 1 retrieved successfully", expectedMessage);

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
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
    void givenNonExistingMessageId_whenRouteGetMessageById_thenThrowResourceNotFoundExceptionHandler() throws URISyntaxException, IOException {
        // given
        Long nonExistingId = 99L;
        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The message with ID 99 does not exist.");

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
        when(messageService.getMessageById(nonExistingId)).thenThrow(new ResourceNotFoundException("message", String.valueOf(nonExistingId)));

        // when
        Exchange result = template.send(MessageRouteEnum.GET_MESSAGE_BY_ID.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid message when route createMessage is called then message created")
    void givenValidMessage_whenRouteCreateMessage_thenMessageCreated() throws IOException, URISyntaxException {
        // given
        MessageDTO inputMessageDTO = new MessageDTO("content", 1L, "userId");
        MessageModel inputMessageModel = new MessageModel("content", 1L, "userId");
        MessageModel createdMessageModel = new MessageModel(inputMessageModel.getContent(), inputMessageModel.getThreadId(), inputMessageModel.getUserId());
        MessageDTO expectedMessage = MessageProcessorMapper.toDTO(createdMessageModel);

        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputMessageDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Message with content " + expectedMessage.getContent() + " created successfully", expectedMessage);

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
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
    void givenMessageExists_whenRouteCreateMessageWithCreatingSameMessage_thenThrowsResourceAlreadyExistExceptionHandler() throws URISyntaxException, IOException {
        // given
        MessageDTO existingMessageDTO = new MessageDTO("content", 1L, "userId");
        MessageModel existingMessageModel = new MessageModel("content", 1L, "userId");

        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setBody(existingMessageDTO);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceAlreadyExistException",
                "The message with name 'name' already exists.");

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
        when(messageService.createMessage(existingMessageModel)).thenThrow(new ResourceAlreadyExistException("message", "name", "name"));

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.CREATE_MESSAGE.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given invalid message when route createMessage is called then throw ResourceBadValueException")
    void givenInvalidMessage_whenRouteCreateMessage_thenMessageCreated() throws IOException, URISyntaxException {
        // given
        MessageDTO inputMessageDTO = new MessageDTO(null, 1L, "userId");
        MessageModel messageModelWithBadValue = MessageProcessorMapper.toModel(inputMessageDTO);

        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputMessageDTO);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceBadValueException",
                "The message has bad value : content.");

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
        when(messageService.createMessage(messageModelWithBadValue)).thenThrow(new ResourceBadValueException("message", "content"));
        doNothing().when(threadExistRequester).verifyThreadIdExistsOrThrow(1L);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.CREATE_MESSAGE.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }


    @Test
    @DisplayName("given valid request when route updateMessage is called then message is updated")
    void givenValidRequest_whenRouteUpdateMessageIsCalled_thenMessageIsUpdated() throws IOException, URISyntaxException {
        // given
        Long messageId = 1L;
        MessageDTO inputMessageDTO = new MessageDTO("content", 1L, "userId");
        MessageModel requestModel = new MessageModel("content", 1L, "userId");
        MessageModel updatedMessage = new MessageModel("content", 1L, "userId");
        MessageDTO expectedMessage = MessageProcessorMapper.toDTO(updatedMessage);

        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", messageId);
        exchange.getIn().setBody(inputMessageDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message with content " + updatedMessage.getContent() + " updated successfully", expectedMessage);

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
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
    void givenInvalidRequest_whenRouteUpdateMessage_thenThrowsResourceNotFoundExceptionHandler() throws URISyntaxException, IOException {
        // given
        Long nonExistingId = 99L;
        MessageDTO inputRequestDTO = new MessageDTO("content", 1L, "userId");
        MessageModel request = new MessageModel("content", 1L, "userId");

        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(inputRequestDTO);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The message with ID 99 does not exist.");

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
        when(messageService.updateMessage(nonExistingId, request)).thenThrow(new ResourceNotFoundException("message", String.valueOf(nonExistingId)));

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.UPDATE_MESSAGE.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

}
