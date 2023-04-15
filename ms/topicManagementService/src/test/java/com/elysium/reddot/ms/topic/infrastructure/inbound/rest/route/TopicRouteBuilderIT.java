package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TopicRouteBuilderIT {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate template;

    @Test
    void givenTopicsExist_whenRouteGetAllTopicsIsCalled_thenAllTopicsAreRetrieved() {
        // arrange
        TopicDTO topic1 = new TopicDTO(1L, "name_1", "Label 1", "Description 1");
        TopicDTO topic2 = new TopicDTO(2L, "name_2", "Label 2", "Description 2");
        List<TopicDTO> topicList = Arrays.asList(topic1, topic2);

        Exchange exchange = new DefaultExchange(camelContext);

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
    void givenExistingTopic_whenRouteGetTopicByIdIsCalledWithValidId_thenCorrectTopicIsReturned() {
        // given
        Long topicId = 1L;
        TopicDTO topic = new TopicDTO(topicId, "name_1", "Label 1", "Description 1");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("id", topicId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with id 1 retrieved successfully", topic);

        // when
        Exchange result = template.send(TopicRouteConstants.GET_TOPIC_BY_ID.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    void givenNonExistingTopicId_whenRouteGetTopicByIdIsCalled_thenThrowResourceNotFoundException() {
        // given
        Long nonExistingId = 99L;
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.NOT_FOUND.value(),
                "The topic with ID 99 does not exist.", null);

        // when
        Exchange result = template.send(TopicRouteConstants.GET_TOPIC_BY_ID.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    void givenValidTopic_whenRouteCreateTopicIsCalled_thenTopicIsCreated() {
        // given
        TopicDTO inputTopic = new TopicDTO(null, "name_3", "Label 3", "Description 3");
        TopicDTO createdTopic = new TopicDTO(3L, inputTopic.getName(), inputTopic.getLabel(), inputTopic.getDescription());

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputTopic);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Topic with name " + createdTopic.getName() + " created successfully", createdTopic);

        // when
        Exchange responseExchange = template.send(TopicRouteConstants.CREATE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    void givenTopicExists_whenRouteCreateTopicIsCalledWithCreatingSameTopic_thenResourceAlreadyExistExceptionHandlerIsTriggered() {
        // given
        TopicDTO existingTopic = new TopicDTO(1L, "name_1", "Label 1", "Description 1");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(existingTopic);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CONFLICT.value(),
                "The topic with name 'name_1' already exists.", null);

        // when
        Exchange responseExchange = template.send(TopicRouteConstants.CREATE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    void givenValidRequest_whenRouteUpdateTopicIsCalled_thenTopicIsUpdated() {
        // given
        Long topicId = 1L;
        TopicDTO request = new TopicDTO(topicId, "name_1", "New label", "New description");
        TopicDTO updatedTopic = new TopicDTO(topicId, "name_1", "New label", "New description");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("id", topicId);
        exchange.getIn().setBody(request);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with name " + updatedTopic.getName() + " updated successfully", updatedTopic);

        // when
        Exchange responseExchange = template.send(TopicRouteConstants.UPDATE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    void givenInvalidRequest_whenRouteUpdateTopicIsCalled_thenResourceNotFoundExceptionIsThrown() {
        // given
        Long nonExistingId = 99L;
        TopicDTO request = new TopicDTO(nonExistingId, "newName", "New label", "New Description");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(request);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.NOT_FOUND.value(),
                "The topic with ID 99 does not exist.", null);

        // when
        Exchange result = template.send(TopicRouteConstants.UPDATE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    void givenInvalidRequestWithBadValue_whenRouteUpdateTopicIsCalled_thenResourceBadValueIsThrown() {
        // given
        Long nonExistingId = 1L;
        TopicDTO request = new TopicDTO(nonExistingId, "name_1", null, "New description");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(request);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.BAD_REQUEST.value(),
                "The topic has bad value : Label is required.", null);

        // when
        Exchange result = template.send(TopicRouteConstants.UPDATE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    void givenTopicExists_whenRouteDeleteTopicIsCalled_thenTopicIsDeleted() {
        // given
        Long topicId = 1L;
        TopicDTO topicDTO = new TopicDTO(topicId, "name_1", "Label 1", "Description 1");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("id", topicId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with id " + topicId + " deleted successfully", topicDTO);

        // when
        Exchange result = template.send(TopicRouteConstants.DELETE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // assert
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    void givenInvalidRequest_whenRouteDeleteTopicIsCalled_thenResourceNotFoundExceptionIsThrown() {
        // given
        Long nonExistingId = 99L;

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.NOT_FOUND.value(),
                "The topic with ID 99 does not exist.", null);

        // when
        Exchange result = template.send(TopicRouteConstants.DELETE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

}
