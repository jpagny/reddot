package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.exception.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.topic.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.topic.application.exception.handler.core.ExceptionHandler;
import com.elysium.reddot.ms.topic.application.exception.handler.exceptionhandler.ResourceAlreadyExistExceptionHandler;
import com.elysium.reddot.ms.topic.application.exception.handler.exceptionhandler.ResourceBadValueExceptionHandler;
import com.elysium.reddot.ms.topic.application.exception.handler.exceptionhandler.ResourceNotFoundExceptionHandler;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
import com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.*;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopicRouteBuilderTest extends CamelTestSupport {

    @Mock
    private TopicApplicationServiceImpl topicService;

    @Override
    protected CamelContext createCamelContext() {
        return new DefaultCamelContext();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        List<ExceptionHandler> listExceptionHandlers = Arrays.asList(
                new ResourceAlreadyExistExceptionHandler(),
                new ResourceBadValueExceptionHandler(),
                new ResourceNotFoundExceptionHandler());
        CamelGlobalExceptionHandler camelGlobalExceptionHandler = new CamelGlobalExceptionHandler(listExceptionHandlers);

        TopicProcessorHolder topicProcessorHolder = new TopicProcessorHolder(
                new GetAllTopicsProcessor(topicService),
                new GetTopicByIdProcessor(topicService),
                new CreateTopicProcessor(topicService),
                new UpdateTopicProcessor(topicService),
                new DeleteTopicProcessor(topicService)
        );

        return new TopicRouteBuilder(camelGlobalExceptionHandler, topicProcessorHolder);
    }

    @Test
    void givenTopicsExist_whenRouteGetAllTopicsIsCalled_thenAllTopicsAreRetrieved() {
        // given
        TopicDTO topic1 = new TopicDTO(1L, "name 1", "Name 1", "Topic 1");
        TopicDTO topic2 = new TopicDTO(2L, "name 2", "Name 2", "Topic 2");
        List<TopicDTO> topicList = Arrays.asList(topic1, topic2);

        Exchange exchange = new DefaultExchange(context);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All topics retrieved successfully", topicList);

        // mock
        when(topicService.getAllTopics()).thenReturn(topicList);

        // when
        Exchange responseExchange = template.send("direct:getAllTopics", exchange);
        ApiResponseDTO actualResponse = responseExchange.getIn().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }


    @Test
    void givenExistingTopic_whenRouteGetTopicByIdIsCalledWithValidId_thenCorrectTopicIsReturned() {
        // given
        Long topicId = 1L;
        TopicDTO topic = new TopicDTO(topicId, "name 1", "Name 1", "Topic 1");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", topicId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with id 1 retrieved successfully", topic);

        // mock
        when(topicService.getTopicById(topicId)).thenReturn(topic);

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
        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.NOT_FOUND.value(),
                "The topic with ID 99 does not exist.", null);

        // mock
        when(topicService.getTopicById(nonExistingId)).thenThrow(new ResourceNotFoundException("topic", String.valueOf(nonExistingId)));

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
        TopicDTO inputTopic = new TopicDTO(null, "name", "Name", "Description");
        TopicDTO createdTopic = new TopicDTO(1L, inputTopic.getName(), inputTopic.getLabel(), inputTopic.getDescription());

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputTopic);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Topic with name " + createdTopic.getName() + " created successfully", createdTopic);

        // mock
        when(topicService.createTopic(inputTopic)).thenReturn(createdTopic);

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
        TopicDTO existingTopic = new TopicDTO(1L, "name", "Name", "Topic description");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setBody(existingTopic);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CONFLICT.value(),
                "The topic with name 'name' already exists.", null);

        // mock
        when(topicService.createTopic(existingTopic)).thenThrow(new ResourceAlreadyExistException("topic", "name", "name"));

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
        TopicDTO request = new TopicDTO(topicId, "newName", "newDescription", "newIcon");
        TopicDTO updatedTopic = new TopicDTO(topicId, "newName", "newDescription", "newIcon");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", topicId);
        exchange.getIn().setBody(request);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with name " + updatedTopic.getName() + " updated successfully", updatedTopic);

        // mock
        when(topicService.updateTopic(topicId, request)).thenReturn(updatedTopic);

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
        TopicDTO request = new TopicDTO(nonExistingId, "newName", "newDescription", "newIcon");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(request);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.NOT_FOUND.value(),
                "The topic with ID 99 does not exist.", null);

        // mock
        when(topicService.updateTopic(nonExistingId, request)).thenThrow(new ResourceNotFoundException("topic", String.valueOf(nonExistingId)));

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
        TopicDTO topicDTO = new TopicDTO(topicId, "test", "Test", "Test topic");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", topicId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with id " + topicId + " deleted successfully", topicDTO);

        // mock
        when(topicService.getTopicById(1L)).thenReturn(topicDTO);

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

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.NOT_FOUND.value(),
                "The topic with ID 99 does not exist.", null);

        // mock
        doThrow(new ResourceNotFoundException("topic", String.valueOf(nonExistingId)))
                .when(topicService).deleteTopicById(nonExistingId);

        // when
        Exchange result = template.send(TopicRouteConstants.DELETE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }


}
