package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.replymessage.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.container.TestContainerSetup;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.constant.ReplyMessageRouteEnum;
import com.elysium.reddot.ms.replymessage.infrastructure.data.exception.GlobalExceptionDTO;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessageProcessorMapper;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.rabbitmq.requester.MessageExistRequester;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReplyMessageRouteBuilderIT extends TestContainerSetup {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate template;

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @MockBean
    private MessageExistRequester messageExistRequester;


    @Test
    @DisplayName("given replyMessages exist when route getAllReplyMessages is called then all replyMessages retrieved")
    void givenReplyMessagesExist_whenRouteGetAllReplyMessages_thenAllReplyMessagesRetrieved() throws Exception {
        // given
        ReplyMessageModel replyMessage1Model = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageModel replyMessage2Model = new ReplyMessageModel("content2", 1L, "userId");
        List<ReplyMessageModel> replyMessageListModel = Arrays.asList(replyMessage1Model, replyMessage2Model);
        List<ReplyMessageDTO> expectedListReplyMessages = ReplyMessageProcessorMapper.toDTOList(replyMessageListModel);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All replies message retrieved successfully", expectedListReplyMessages);

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteEnum.GET_ALL_REPLIES_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }


    @Test
    @DisplayName("given existing replyMessage when route getReplyMessageById is called with valid id then replyMessage returned")
    void givenExistingReplyMessage_whenRouteGetReplyMessageByIdWithValidId_thenReplyMessageReturned() throws Exception {
        // given
        Long replyMessageId = 1L;
        ReplyMessageDTO expectedReplyMessage = new ReplyMessageDTO("content", 1L, "userId");

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", replyMessageId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Reply message with id 1 retrieved successfully", expectedReplyMessage);

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteEnum.GET_REPLY_MESSAGE_BY_ID.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given non-existing replyMessage id when route getReplyMessageById is called then throw ResourceNotFoundExceptionHandler")
    void givenNonExistingReplyMessageId_whenRouteGetReplyMessageById_thenThrowResourceNotFoundExceptionHandler() throws Exception {
        // given
        Long nonExistingId = 99L;

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The reply message with ID 99 does not exist.");

        // when
        Exchange result = template.send(ReplyMessageRouteEnum.GET_REPLY_MESSAGE_BY_ID.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid replyMessage when route createReplyMessage is called then replyMessage created")
    void givenValidReplyMessage_whenRouteCreateReplyMessage_thenReplyMessageCreated() throws Exception {
        // given
        ReplyMessageDTO inputReplyMessageDTO = new ReplyMessageDTO("content", 1L, "userId");
        ReplyMessageModel inputReplyMessageModel = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageModel createdReplyMessageModel = new ReplyMessageModel(inputReplyMessageModel.getContent(), inputReplyMessageModel.getParentMessageID(), inputReplyMessageModel.getUserId());
        ReplyMessageDTO expectedReplyMessage = ReplyMessageProcessorMapper.toDTO(createdReplyMessageModel);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputReplyMessageDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Reply message with content " + expectedReplyMessage.getContent() + " created successfully", expectedReplyMessage);

        // mock
        doNothing().when(messageExistRequester).verifyMessageIdExistsOrThrow(any());

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteEnum.CREATE_REPLY_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given replyMessage exists when route createReplyMessage is called with creating same replyMessage then throws ResourceAlreadyExistExceptionHandler")
    void givenReplyMessageExists_whenRouteCreateReplyMessageWithCreatingSameReplyMessage_thenThrowsResourceAlreadyExistExceptionHandler() throws Exception {
        // given
        ReplyMessageDTO existingReplyMessageDTO = new ReplyMessageDTO("content_1", 1L, "userId");

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setBody(existingReplyMessageDTO);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceAlreadyExistException",
                "The reply message with content 'content_1' already exists.");

        // mock
        doNothing().when(messageExistRequester).verifyMessageIdExistsOrThrow(1L);

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteEnum.CREATE_REPLY_MESSAGE.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid request when route updateReplyMessage is called then replyMessage is updated")
    void givenValidRequest_whenRouteUpdateReplyMessageIsCalled_thenReplyMessageIsUpdated() throws Exception {
        // given
        Long replyMessageId = 1L;
        ReplyMessageDTO inputReplyMessageDTO = new ReplyMessageDTO("content", 1L, "userId");
        ReplyMessageModel updatedReplyMessage = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageDTO expectedReplyMessage = ReplyMessageProcessorMapper.toDTO(updatedReplyMessage);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", replyMessageId);
        exchange.getIn().setBody(inputReplyMessageDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Reply message updated successfully", expectedReplyMessage);

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteEnum.UPDATE_REPLY_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given invalid request when route updateReplyMessage is called then throws ResourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteUpdateReplyMessage_thenThrowsResourceNotFoundExceptionHandler() throws Exception {
        // given
        Long nonExistingId = 99L;
        ReplyMessageDTO inputRequestDTO = new ReplyMessageDTO("content", 1L, "userId");

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(inputRequestDTO);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The reply message with ID 99 does not exist.");

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteEnum.UPDATE_REPLY_MESSAGE.getRouteName(), exchange);
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
            JsonNode jsonNode = new org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper().readTree(responseBody);
            return jsonNode.get("access_token").asText();
        } else {
            throw new RuntimeException("No response body");
        }

    }
}
