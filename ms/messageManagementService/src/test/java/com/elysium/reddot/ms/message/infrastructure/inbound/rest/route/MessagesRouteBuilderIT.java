package com.elysium.reddot.ms.message.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.message.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.message.container.TestContainerSetup;
import com.elysium.reddot.ms.message.infrastructure.data.dto.GlobalExceptionDTO;
import com.elysium.reddot.ms.message.infrastructure.inbound.constant.MessageRouteEnum;
import com.elysium.reddot.ms.message.infrastructure.outbound.rabbitmq.requester.ThreadExistRequester;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("test")
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


    @Test
    @DisplayName("given no auth when get messages then unauthorized")
    void givenNoAuth_whenGetMessages_thenUnauthorized() throws JsonProcessingException {
        // given
        Exchange exchange = new DefaultExchange(camelContext);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("TokenNotValidException",
                "Token is not validate.");

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.GET_ALL_MESSAGES.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given authenticated user with token when get messages then success")
    void givenAuthenticatedUserWithToken_whenGetMessages_thenSuccess() throws Exception {
        // given
        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.GET_ALL_MESSAGES.getRouteName(), exchange);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseExchange.getMessage().getBody(String.class), ApiResponseDTO.class);

        // then
        assertEquals(HttpStatus.OK.value(), actualResponse.getStatus());
    }

    @Test
    @DisplayName("given messages exist when route getAllMessages is called then all messages retrieved")
    void givenMessagesExist_whenRouteGetAllMessages_thenAllMessagesAreRetrieved() throws Exception {
        // arrange
        LocalDateTime localDateTime = LocalDateTime.now();
        MessageDTO message1 = new MessageDTO(1L, "content_1", 1L, "userId", localDateTime, localDateTime);
        MessageDTO message2 = new MessageDTO(2L, "content_2", 1L, "userId", localDateTime, localDateTime);
        List<MessageDTO> messageList = Arrays.asList(message1, message2);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All messages retrieved successfully", messageList);

        // act
        Exchange responseExchange = template.send("direct:getAllMessages", exchange);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseExchange.getMessage().getBody(String.class), ApiResponseDTO.class);

        // assert
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

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", messageId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message with id 1 retrieved successfully", message);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.GET_MESSAGE_BY_ID.getRouteName(), exchange);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseExchange.getMessage().getBody(String.class), ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given non-existing message id when route getMessageById then throw ResourceNotFoundExceptionHandler")
    void givenNonExistingMessageId_whenRouteGetMessageById_thenThrowResourceNotFoundExceptionHandler() throws Exception {
        // given
        Long nonExistingId = 99L;
        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The message with ID 99 does not exist.");

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.GET_MESSAGE_BY_ID.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

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
        MessageDTO createdMessage = new MessageDTO(3L, inputMessage.getContent(), inputMessage.getThreadId(), inputMessage.getUserId());

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputMessage);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Message with content " + createdMessage.getContent() + " created successfully", createdMessage);

        // mock
        doNothing().when(threadExistRequester).verifyThreadIdExistsOrThrow(1L);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.CREATE_MESSAGE.getRouteName(), exchange);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseExchange.getMessage().getBody(String.class), ApiResponseDTO.class);

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

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputMessage);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The Topic id with ID 1 does not exist.");

        // mock
        doThrow(new ResourceNotFoundException("Topic id", "1")).when(threadExistRequester).verifyThreadIdExistsOrThrow(1L);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.CREATE_MESSAGE.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

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

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputMessage);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceBadValueException",
                "The message has bad value : content is required and cannot be empty.");

        // mock
        doNothing().when(threadExistRequester).verifyThreadIdExistsOrThrow(1L);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.CREATE_MESSAGE.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

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

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setBody(existingMessage);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceAlreadyExistException",
                "The Message with content 'content_1' already exists.");

        // mock
        doNothing().when(threadExistRequester).verifyThreadIdExistsOrThrow(1L);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.CREATE_MESSAGE.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

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
        MessageDTO updatedMessage = new MessageDTO(messageId, "content_updated_1", 1L, "userId", localDateTime, localDateTime);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", messageId);
        exchange.getIn().setBody(request);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Message with content " + updatedMessage.getContent() + " updated successfully", updatedMessage);

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.UPDATE_MESSAGE.getRouteName(), exchange);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseExchange.getMessage().getBody(String.class), ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given invalid request when route updateMessage is called then throws ResourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteUpdateMessage_thenResourceThrowsNotFoundExceptionHandler() throws Exception {
        // given
        Long nonExistingId = 99L;
        LocalDateTime localDateTime = LocalDateTime.now();
        MessageDTO request = new MessageDTO(nonExistingId, "content_1", 1L, "userId", localDateTime, localDateTime);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(request);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The message with ID 99 does not exist.");

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.UPDATE_MESSAGE.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given invalid request with bad value when route updateMessage is called then throws ResourceBadValueHandler")
    void givenInvalidRequestWithBadValue_whenRouteUpdateMessage_thenThrowsResourceBadValueHandler() throws Exception {
        // given
        Long messageId = 1L;
        LocalDateTime localDateTime = LocalDateTime.now();
        MessageDTO request = new MessageDTO(messageId, null, 1L, "userId", localDateTime, localDateTime);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", messageId);
        exchange.getIn().setBody(request);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceBadValueException",
                "The message has bad value : content is required and cannot be empty.");

        // when
        Exchange responseExchange = template.send(MessageRouteEnum.UPDATE_MESSAGE.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }


    private String obtainAccessToken(String username, String password) throws Exception {

        HttpClient httpClient = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder("http://localhost:11003/realms/reddot/protocol/openid-connect/token");
        String requestBody = "grant_type=password&client_id=reddot-app&client_secret=H80mMKQZYyXf9S7yQ2cEAxRmXud0uCmU"
                + "&username=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);
        StringEntity stringEntity = new StringEntity(requestBody, ContentType.APPLICATION_FORM_URLENCODED);

        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setEntity(stringEntity);

        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String responseBody = EntityUtils.toString(entity);
            JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
            return jsonNode.get("access_token").asText();
        } else {
            throw new RuntimeException("No response body");
        }

    }


}
