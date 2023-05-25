package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.container.TestContainerSetup;
import com.elysium.reddot.ms.topic.infrastructure.constant.TopicRouteEnum;
import com.elysium.reddot.ms.topic.infrastructure.data.dto.GlobalExceptionDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TopicRouteBuilderIT extends TestContainerSetup {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate template;

    @Test
    @DisplayName("given no auth when get topics then unauthorized")
    void givenNoAuth_whenGetTopics_thenUnauthorized() {
        // given
        Exchange exchange = new DefaultExchange(camelContext);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("TokenNotValidException",
                "Token is not validate.");

        // when
        Exchange result = template.send(TopicRouteEnum.GET_ALL_TOPICS.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given authenticated user with token when get topics then success")
    void givenAuthenticatedUserWithToken_whenGetTopics_thenSuccess() throws Exception {
        // given
        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);

        // when
        Exchange result = template.send(TopicRouteEnum.GET_ALL_TOPICS.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(HttpStatus.OK.value(), actualResponse.getStatus());
    }

    @Test
    @DisplayName("given topics exist when route getAllTopics is called then all topics retrieved")
    void givenTopicsExist_whenRouteGetAllTopics_thenAllTopicsAreRetrieved() throws Exception {
        // arrange
        TopicDTO topic1 = new TopicDTO(1L, "name_1", "Label 1", "Description 1");
        TopicDTO topic2 = new TopicDTO(2L, "name_2", "Label 2", "Description 2");
        List<TopicDTO> topicList = Arrays.asList(topic1, topic2);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All topics retrieved successfully", topicList);

        // act
        Exchange responseExchange = template.send("direct:getAllTopics", exchange);
        ApiResponseDTO actualResponse = responseExchange.getIn().getBody(ApiResponseDTO.class);

        // assert
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given existing topic when route getTopicById is called with valid id then topic returned")
    void givenExistingTopic_whenRouteGetTopicByIdWithValidId_thenTopicReturned() throws Exception {
        // given
        Long topicId = 1L;
        TopicDTO topic = new TopicDTO(topicId, "name_1", "Label 1", "Description 1");

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", topicId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with id 1 retrieved successfully", topic);

        // when
        Exchange result = template.send(TopicRouteEnum.GET_TOPIC_BY_ID.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given non-existing topic id when route getTopicById then throw ResourceNotFoundExceptionHandler")
    void givenNonExistingTopicId_whenRouteGetTopicById_thenThrowResourceNotFoundExceptionHandler() throws Exception {
        // given
        Long nonExistingId = 99L;
        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The topic with ID 99 does not exist.");

        // when
        Exchange result = template.send(TopicRouteEnum.GET_TOPIC_BY_ID.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid topic when route createTopic is called then topic created")
    void givenValidTopic_whenRouteCreateTopic_thenTopicCreated() throws Exception {
        // given
        TopicDTO inputTopic = new TopicDTO(null, "name_3", "Label 3", "Description 3");
        TopicDTO createdTopic = new TopicDTO(3L, inputTopic.getName(), inputTopic.getLabel(), inputTopic.getDescription());

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputTopic);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Topic with name " + createdTopic.getName() + " created successfully", createdTopic);

        // when
        Exchange responseExchange = template.send(TopicRouteEnum.CREATE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given topic exists when route createTopic is called with creating same topic then throws resourceAlreadyExistExceptionHandler")
    void givenTopicExists_whenRouteCreateTopicWithCreatingSameTopic_thenThrowsResourceAlreadyExistExceptionHandler() throws Exception {
        // given
        TopicDTO existingTopic = new TopicDTO(1L, "name_1", "Label 1", "Description 1");

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setBody(existingTopic);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceAlreadyExistException",
                "The topic with name 'name_1' already exists.");

        // when
        Exchange responseExchange = template.send(TopicRouteEnum.CREATE_TOPIC.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid request when route updateTopic is called then topic updated")
    void givenValidRequest_whenRouteUpdateTopic_thenTopicUpdated() throws Exception {
        // given
        Long topicId = 1L;
        TopicDTO request = new TopicDTO(topicId, "name_1", "New label", "New description");
        TopicDTO updatedTopic = new TopicDTO(topicId, "name_1", "New label", "New description");

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", topicId);
        exchange.getIn().setBody(request);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with name " + updatedTopic.getName() + " updated successfully", updatedTopic);

        // when
        Exchange responseExchange = template.send(TopicRouteEnum.UPDATE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given invalid request when route updateTopic is called then throws ResourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteUpdateTopic_thenResourceThrowsNotFoundExceptionHandler() throws Exception {
        // given
        Long nonExistingId = 99L;
        TopicDTO request = new TopicDTO(nonExistingId, "newName", "New label", "New Description");

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(request);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The topic with ID 99 does not exist.");

        // when
        Exchange result = template.send(TopicRouteEnum.UPDATE_TOPIC.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given invalid request with bad value when route updateTopic is called then throws ResourceBadValueHandler")
    void givenInvalidRequestWithBadValue_whenRouteUpdateTopic_thenThrowsResourceBadValueHandler() throws Exception {
        // given
        Long nonExistingId = 1L;
        TopicDTO request = new TopicDTO(nonExistingId, "name_1", null, "New description");

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(request);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceBadValueException",
                "The topic has bad value : label is required and cannot be empty.");

        // when
        Exchange result = template.send(TopicRouteEnum.UPDATE_TOPIC.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given topic exists when route deleteTopic is called then topic deleted")
    void givenTopicExists_whenRouteDeleteTopic_thenTopicDeleted() throws Exception {
        // given
        Long topicId = 1L;
        TopicDTO topicDTO = new TopicDTO(topicId, "name_1", "Label 1", "Description 1");

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", topicId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with id " + topicId + " deleted successfully", topicDTO);

        // when
        Exchange result = template.send(TopicRouteEnum.DELETE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // assert
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given invalid request when route deleteTopic is called then throws ResourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteDeleteTopic_thenResourceNotFoundExceptionHandler() throws Exception {
        // given
        Long nonExistingId = 99L;

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The topic with ID 99 does not exist.");

        // when
        Exchange result = template.send(TopicRouteEnum.DELETE_TOPIC.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

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
