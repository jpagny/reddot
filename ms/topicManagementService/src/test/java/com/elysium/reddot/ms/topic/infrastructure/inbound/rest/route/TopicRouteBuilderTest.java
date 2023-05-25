package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.topic.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.application.service.KeycloakService;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.infrastructure.constant.TopicRouteEnum;
import com.elysium.reddot.ms.topic.infrastructure.data.dto.GlobalExceptionDTO;
import com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.exception.GlobalExceptionHandler;
import com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.keycloak.CheckTokenProcessor;
import com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.keycloak.KeycloakProcessorHolder;
import com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.topic.*;
import com.elysium.reddot.ms.topic.infrastructure.mapper.TopicProcessorMapper;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopicRouteBuilderTest extends CamelTestSupport {

    @Mock
    private TopicApplicationServiceImpl topicService;

    @Mock
    private KeycloakService keycloakService;

    @Override
    protected CamelContext createCamelContext() {
        return new DefaultCamelContext();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        TopicProcessorHolder topicProcessorHolder = new TopicProcessorHolder(
                new GetAllTopicsProcessor(topicService),
                new GetTopicByIdProcessor(topicService),
                new CreateTopicProcessor(topicService),
                new UpdateTopicProcessor(topicService),
                new DeleteTopicProcessor(topicService)
        );

        KeycloakProcessorHolder keycloakProcessorHolder = new KeycloakProcessorHolder(
                new CheckTokenProcessor(keycloakService)
        );

        return new TopicRouteBuilder(globalExceptionHandler, topicProcessorHolder, keycloakProcessorHolder);
    }

    @Test
    @DisplayName("given the token is inactive, when getAllTopics route is processed, then TokenNotActiveException is returned")
    void givenInactiveToken_whenRouteGetAllTopics_thenTokenNotActiveExceptionReturned() throws Exception {
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
        Exchange responseExchange = template.send(TopicRouteEnum.GET_ALL_TOPICS.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("Given an active token without user role, when getAllTopics route is processed, then HasNotRoleException is returned")
    void givenActiveTokenWithoutUserRole_whenRouteGetAllTopics_thenHasNotRoleExceptionReturned() throws Exception {
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
        Exchange responseExchange = template.send(TopicRouteEnum.GET_ALL_TOPICS.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }


    @Test
    @DisplayName("given topics exist when route getAllTopics is called then all topics retrieved")
    void givenTopicsExist_whenRouteGetAllTopics_thenAllTopicsRetrieved() throws URISyntaxException, IOException {
        // given
        TopicModel topic1Model = new TopicModel(1L, "name 1", "Name 1", "Topic 1");
        TopicModel topic2Model = new TopicModel(2L, "name 2", "Name 2", "Topic 2");
        List<TopicModel> topicListModel = Arrays.asList(topic1Model, topic2Model);
        List<TopicDTO> expectedListTopics = TopicProcessorMapper.toDTOList(topicListModel);

        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All topics retrieved successfully", expectedListTopics);

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
        when(topicService.getAllTopics()).thenReturn(topicListModel);

        // when
        Exchange responseExchange = template.send("direct:getAllTopics", exchange);
        ApiResponseDTO actualResponse = responseExchange.getIn().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }


    @Test
    @DisplayName("given existing topic when route getTopicById is called with valid id then topic returned")
    void givenExistingTopic_whenRouteGetTopicByIdWithValidId_thenTopicReturned() throws URISyntaxException, IOException {
        // given
        Long topicId = 1L;
        TopicModel topic = new TopicModel(topicId, "name 1", "Name 1", "Topic 1");
        TopicDTO expectedTopic = new TopicDTO(topicId, "name 1", "Name 1", "Topic 1");

        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", topicId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with id 1 retrieved successfully", expectedTopic);

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
        when(topicService.getTopicById(topicId)).thenReturn(topic);

        // when
        Exchange result = template.send(TopicRouteEnum.GET_TOPIC_BY_ID.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given non-existing topic id when route getTopicById is called then throw ResourceNotFoundExceptionHandler")
    void givenNonExistingTopicId_whenRouteGetTopicById_thenThrowResourceNotFoundExceptionHandler() throws URISyntaxException, IOException {
        // given
        Long nonExistingId = 99L;
        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The topic with ID 99 does not exist.");

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
        when(topicService.getTopicById(nonExistingId)).thenThrow(new ResourceNotFoundException("topic", String.valueOf(nonExistingId)));

        // when
        Exchange result = template.send(TopicRouteEnum.GET_TOPIC_BY_ID.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid topic when route createTopic is called then topic created")
    void givenValidTopic_whenRouteCreateTopic_thenTopicCreated() throws URISyntaxException, IOException {
        // given
        TopicDTO inputTopicDTO = new TopicDTO(null, "name", "Name", "Description");
        TopicModel inputTopicModel = new TopicModel(null, "name", "Name", "Description");
        TopicModel createdTopicModel = new TopicModel(1L, inputTopicModel.getName(), inputTopicModel.getLabel(), inputTopicModel.getDescription());
        TopicDTO expectedTopic = TopicProcessorMapper.toDTO(createdTopicModel);

        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputTopicDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Topic with name " + expectedTopic.getName() + " created successfully", expectedTopic);

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
        when(topicService.createTopic(inputTopicModel)).thenReturn(createdTopicModel);

        // when
        Exchange responseExchange = template.send(TopicRouteEnum.CREATE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given topic exists when route createTopic is called with creating same topic then throws ResourceAlreadyExistExceptionHandler")
    void givenTopicExists_whenRouteCreateTopicWithCreatingSameTopic_thenThrowsResourceAlreadyExistExceptionHandler() throws URISyntaxException, IOException {
        // given
        TopicDTO existingTopicDTO = new TopicDTO(1L, "name", "Name", "Topic description");
        TopicModel existingTopicModel = new TopicModel(1L, "name", "Name", "Topic description");

        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setBody(existingTopicDTO);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceAlreadyExistException",
                "The topic with name 'name' already exists.");

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
        when(topicService.createTopic(existingTopicModel)).thenThrow(new ResourceAlreadyExistException("topic", "name", "name"));

        // when
        Exchange responseExchange = template.send(TopicRouteEnum.CREATE_TOPIC.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid request when route updateTopic is called then topic is updated")
    void givenValidRequest_whenRouteUpdateTopicIsCalled_thenTopicIsUpdated() throws URISyntaxException, IOException {
        // given
        Long topicId = 1L;
        TopicDTO inputTopicDTO = new TopicDTO(topicId, "newName", "newDescription", "newIcon");
        TopicModel requestModel = new TopicModel(topicId, "newName", "newDescription", "newIcon");
        TopicModel updatedTopic = new TopicModel(topicId, "newName", "newDescription", "newIcon");
        TopicDTO expectedTopic = TopicProcessorMapper.toDTO(updatedTopic);

        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", topicId);
        exchange.getIn().setBody(inputTopicDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with name " + updatedTopic.getName() + " updated successfully", expectedTopic);

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
        when(topicService.updateTopic(topicId, requestModel)).thenReturn(updatedTopic);

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
    void givenInvalidRequest_whenRouteUpdateTopic_thenThrowsResourceNotFoundExceptionHandler() throws URISyntaxException, IOException {
        // given
        Long nonExistingId = 99L;
        TopicDTO inputRequestDTO = new TopicDTO(nonExistingId, "newName", "newDescription", "newIcon");
        TopicModel request = new TopicModel(nonExistingId, "newName", "newDescription", "newIcon");

        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(inputRequestDTO);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The topic with ID 99 does not exist.");

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
        when(topicService.updateTopic(nonExistingId, request)).thenThrow(new ResourceNotFoundException("topic", String.valueOf(nonExistingId)));

        // when
        Exchange responseExchange = template.send(TopicRouteEnum.UPDATE_TOPIC.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given topic exists when route deleteTopic is called then topic deleted")
    void givenTopicExists_whenRouteDeleteTopic_thenTopicDeleted() throws URISyntaxException, IOException {
        // given
        Long topicId = 1L;
        TopicModel topicModel = new TopicModel(topicId, "test", "Test", "Test topic");
        TopicDTO expectedTopic = new TopicDTO(topicId, "test", "Test", "Test topic");

        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", topicId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with id " + topicId + " deleted successfully", expectedTopic);

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
        when(topicService.deleteTopicById(1L)).thenReturn(topicModel);

        // when
        Exchange result = template.send(TopicRouteEnum.DELETE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given invalid request when route deleteTopic is called then throws resourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteDeleteTopic_thenThrowsResourceNotFoundExceptionHandler() throws URISyntaxException, IOException {
        // given
        Long nonExistingId = 99L;

        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The topic with ID 99 does not exist.");

        // mock
        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);
        doThrow(new ResourceNotFoundException("topic", String.valueOf(nonExistingId)))
                .when(topicService).deleteTopicById(nonExistingId);

        // when
        Exchange responseExchange = template.send(TopicRouteEnum.DELETE_TOPIC.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }


}
