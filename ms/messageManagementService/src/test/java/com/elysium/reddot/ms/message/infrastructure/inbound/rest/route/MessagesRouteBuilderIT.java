package com.elysium.reddot.ms.message.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.message.container.TestContainerSetup;
import com.elysium.reddot.ms.message.infrastructure.constant.MessageRouteEnum;
import com.elysium.reddot.ms.message.infrastructure.data.dto.GlobalExceptionDTO;
import com.elysium.reddot.ms.message.infrastructure.outbound.rabbitmq.requester.ThreadExistRequester;
import com.elysium.reddot.ms.message.utils.KeycloakTestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MessagesRouteBuilderIT extends TestContainerSetup {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate template;

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @MockBean
    private ThreadExistRequester threadExistRequester;

    private KeycloakTestUtils.UserToken userToken;

    @BeforeEach
    public void setup() throws Exception {
        userToken = KeycloakTestUtils.obtainAccessToken("user1", "test");
        KeycloakAuthenticationToken auth = KeycloakTestUtils.createAuthenticationToken(userToken, "user");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("given authenticated user with token when get messages then success")
    void givenAuthenticatedUserWithToken_whenGetMessages_thenSuccess() throws Exception {
        // given
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.GET_ALL_MESSAGES.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(HttpStatus.OK.value(), actualResponse.getStatus());
    }

    @Test
    @DisplayName("given messages exist when route getAllMessages is called then all messages retrieved")
    void givenMessagesExist_whenRouteGetAllMessages_thenAllMessagesAreRetrieved() throws Exception {
        // given
        LocalDateTime localDateTime = LocalDateTime.now();
        MessageDTO message1 = new MessageDTO(1L, "content_1", 1L, "userId", localDateTime, localDateTime);
        MessageDTO message2 = new MessageDTO(2L, "content_2", 1L, "userId", localDateTime, localDateTime);
        List<MessageDTO> messageList = Arrays.asList(message1, message2);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All messages retrieved successfully", messageList);

        // when
        Exchange responseExchange = template.send("direct:getAllMessages", exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given existing message when route getMessageById is called with valid id then message returned")
    void givenExistingMessage_whenRouteGetMessageByIdWithValidId_thenMessageReturned() throws Exception {
        // given
        Long messageId = 1L;
        LocalDateTime localDateTime = LocalDateTime.now();
        MessageDTO message = new MessageDTO(1L, "content_1", 1L, "userId", localDateTime, localDateTime);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader("id", messageId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message with id 1 retrieved successfully", message);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.GET_MESSAGE_BY_ID.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given non-existing message id when route getMessageById then throw ResourceNotFoundExceptionHandler")
    void givenNonExistingMessageId_whenRouteGetMessageById_thenThrowResourceNotFoundExceptionHandler() throws JsonProcessingException {
        // given
        Long nonExistingId = 99L;
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The message with ID 99 does not exist.");

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
    void givenValidMessage_whenRouteCreateMessage_thenMessageCreated() throws Exception {
        // given
        LocalDateTime localDateTime = LocalDateTime.now();
        MessageDTO inputMessage = new MessageDTO(null, "content_3", 1L, "userId", localDateTime, localDateTime);
        String requestJson = objectMapper.writeValueAsString(inputMessage);

        MessageDTO createdMessage = new MessageDTO(3L, inputMessage.getContent(), inputMessage.getThreadId(), inputMessage.getUserId());

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(requestJson);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Message with content " + createdMessage.getContent() + " created successfully", createdMessage);

        // mock
        doNothing().when(threadExistRequester).verifyThreadIdExistsOrThrow(1L);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.CREATE_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given invalid topic when route createMessage is called then throws ResourceNotFoundException")
    void givenInvalidTopic_whenRouteCreateMessage_thenThrowsResourceNotFoundException() throws Exception {
        // given
        LocalDateTime localDateTime = LocalDateTime.now();
        MessageDTO inputMessage = new MessageDTO(null, "content_3", 1L, "userId", localDateTime, localDateTime);
        String requestJson = objectMapper.writeValueAsString(inputMessage);


        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(requestJson);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The Topic id with ID 1 does not exist.");

        // mock
        doThrow(new ResourceNotFoundException("Topic id", "1")).when(threadExistRequester).verifyThreadIdExistsOrThrow(1L);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.CREATE_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        GlobalExceptionDTO actualResponse = objectMapper.readValue(responseJson, GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given invalid message when route createMessage is called then throws ResourceBadValueException")
    void givenInvalidMessage_whenRouteCreateMessage_thenThrowsResourceBadValueException() throws Exception {
        // given
        LocalDateTime localDateTime = LocalDateTime.now();
        MessageDTO inputMessage = new MessageDTO(null, null, 1L, "userId", localDateTime, localDateTime);
        String requestJson = objectMapper.writeValueAsString(inputMessage);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(requestJson);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceBadValueException",
                "The message has bad value : content is required and cannot be empty.");

        // mock
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
    @DisplayName("given message exists when route createMessage is called with creating same message then throws resourceAlreadyExistExceptionHandler")
    void givenMessageExists_whenRouteCreateMessageWithCreatingSameMessage_thenThrowsResourceAlreadyExistExceptionHandler() throws Exception {
        // given
        LocalDateTime localDateTime = LocalDateTime.now();
        MessageDTO existingMessage = new MessageDTO(1L, "content_1", 1L, "userId", localDateTime, localDateTime);
        String requestJson = objectMapper.writeValueAsString(existingMessage);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setBody(requestJson);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceAlreadyExistException",
                "The message with content 'content_1' already exists with board id 1.");

        // mock
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
    @DisplayName("given valid request when route updateMessage is called then message updated")
    void givenValidRequest_whenRouteUpdateMessage_thenMessageUpdated() throws Exception {
        // given
        Long messageId = 1L;
        LocalDateTime localDateTime = LocalDateTime.now();
        MessageDTO request = new MessageDTO(messageId, "content_updated_1", 1L, "userId", localDateTime, localDateTime);
        String requestJson = objectMapper.writeValueAsString(request);

        MessageDTO updatedMessage = new MessageDTO(messageId, "content_updated_1", 1L, "userId", localDateTime, localDateTime);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader("id", messageId);
        exchange.getIn().setBody(requestJson);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message with content " + updatedMessage.getContent() + " updated successfully", updatedMessage);

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
    void givenInvalidRequest_whenRouteUpdateMessage_thenResourceThrowsNotFoundExceptionHandler() throws JsonProcessingException {
        // given
        Long nonExistingId = 99L;
        LocalDateTime localDateTime = LocalDateTime.now();
        MessageDTO request = new MessageDTO(nonExistingId, "content_1", 1L, "userId", localDateTime, localDateTime);
        String requestJson = objectMapper.writeValueAsString(request);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(requestJson);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The message with ID 99 does not exist.");

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.UPDATE_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        GlobalExceptionDTO actualResponse = objectMapper.readValue(responseJson, GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given invalid request with bad value when route updateMessage is called then throws ResourceBadValueHandler")
    void givenInvalidRequestWithBadValue_whenRouteUpdateMessage_thenThrowsResourceBadValueHandler() throws JsonProcessingException {
        // given
        Long messageId = 1L;
        LocalDateTime localDateTime = LocalDateTime.now();
        MessageDTO request = new MessageDTO(messageId, null, 1L, "userId", localDateTime, localDateTime);
        String requestJson = objectMapper.writeValueAsString(request);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader("id", messageId);
        exchange.getIn().setBody(requestJson);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceBadValueException",
                "The message has bad value : content is required and cannot be empty.");

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.UPDATE_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        GlobalExceptionDTO actualResponse = objectMapper.readValue(responseJson, GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given different user request when route updateMessage is called then throw IsNotOwnerMessageException")
    void givenDifferentUserRequest_whenRouteUpdateMessage_thenThrowIsNotOwnerMessageException() throws Exception {
        // given
        Long messageId = 3L;
        LocalDateTime localDateTime = LocalDateTime.now();
        MessageDTO request = new MessageDTO(messageId, "content_updated_1", 1L, "", localDateTime, localDateTime);
        String requestJson = objectMapper.writeValueAsString(request);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader("id", messageId);
        exchange.getIn().setBody(requestJson);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("IsNotOwnerMessageException",
                "You are not owner this message. You can't update this message");

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.UPDATE_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        GlobalExceptionDTO actualResponse = objectMapper.readValue(responseJson, GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

}
