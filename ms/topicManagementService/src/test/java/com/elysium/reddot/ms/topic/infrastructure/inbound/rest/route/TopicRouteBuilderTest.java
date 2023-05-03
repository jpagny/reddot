package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.exception.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.topic.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.topic.application.exception.handler.core.IExceptionHandler;
import com.elysium.reddot.ms.topic.application.exception.handler.exceptionHandler.ResourceAlreadyExistIExceptionHandler;
import com.elysium.reddot.ms.topic.application.exception.handler.exceptionHandler.ResourceBadValueIExceptionHandler;
import com.elysium.reddot.ms.topic.application.exception.handler.exceptionHandler.ResourceNotFoundIExceptionHandler;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.*;
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
        List<IExceptionHandler> listIExceptionHandlers = Arrays.asList(
                new ResourceAlreadyExistIExceptionHandler(),
                new ResourceBadValueIExceptionHandler(),
                new ResourceNotFoundIExceptionHandler());
        CamelGlobalExceptionHandler camelGlobalExceptionHandler = new CamelGlobalExceptionHandler(listIExceptionHandlers);

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
    @DisplayName("given topics exist when route getAllTopics is called then all topics retrieved")
    void givenTopicsExist_whenRouteGetAllTopics_thenAllTopicsRetrieved() {
        // given
        TopicModel topic1Model = new TopicModel(1L, "name 1", "Name 1", "Topic 1");
        TopicModel topic2Model = new TopicModel(2L, "name 2", "Name 2", "Topic 2");
        List<TopicModel> topicListModel = Arrays.asList(topic1Model, topic2Model);
        List<TopicDTO> expectedListTopics = TopicProcessorMapper.toDTOList(topicListModel);

        Exchange exchange = new DefaultExchange(context);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All topics retrieved successfully", expectedListTopics);

        // mock
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
    void givenExistingTopic_whenRouteGetTopicByIdWithValidId_thenTopicReturned() {
        // given
        Long topicId = 1L;
        TopicModel topic = new TopicModel(topicId, "name 1", "Name 1", "Topic 1");
        TopicDTO expectedTopic =  new TopicDTO(topicId, "name 1", "Name 1", "Topic 1");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", topicId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with id 1 retrieved successfully", expectedTopic);

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
    @DisplayName("given non-existing topic id when route getTopicById is called then throw ResourceNotFoundExceptionHandler")
    void givenNonExistingTopicId_whenRouteGetTopicById_thenThrowResourceNotFoundExceptionHandler() {
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
    @DisplayName("given valid topic when route createTopic is called then topic created")
    void givenValidTopic_whenRouteCreateTopic_thenTopicCreated() {
        // given
        TopicDTO inputTopicDTO = new TopicDTO(null, "name", "Name", "Description");
        TopicModel inputTopicModel = new TopicModel(null, "name", "Name", "Description");
        TopicModel createdTopicModel = new TopicModel(1L, inputTopicModel.getName(), inputTopicModel.getLabel(), inputTopicModel.getDescription());
        TopicDTO expectedTopic = TopicProcessorMapper.toDTO(createdTopicModel);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputTopicDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Topic with name " + expectedTopic.getName() + " created successfully", expectedTopic);

        // mock
        when(topicService.createTopic(inputTopicModel)).thenReturn(createdTopicModel);

        // when
        Exchange responseExchange = template.send(TopicRouteConstants.CREATE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given topic exists when route createTopic is called with creating same topic then throws ResourceAlreadyExistExceptionHandler")
    void givenTopicExists_whenRouteCreateTopicWithCreatingSameTopic_thenThrowsResourceAlreadyExistExceptionHandler() {
        // given
        TopicDTO existingTopicDTO = new TopicDTO(1L, "name", "Name", "Topic description");
        TopicModel existingTopicModel = new TopicModel(1L, "name", "Name", "Topic description");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setBody(existingTopicDTO);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CONFLICT.value(),
                "The topic with name 'name' already exists.", null);

        // mock
        when(topicService.createTopic(existingTopicModel)).thenThrow(new ResourceAlreadyExistException("topic", "name", "name"));

        // when
        Exchange responseExchange = template.send(TopicRouteConstants.CREATE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given valid request when route updateTopic is called then topic is updated")
    void givenValidRequest_whenRouteUpdateTopicIsCalled_thenTopicIsUpdated() {
        // given
        Long topicId = 1L;
        TopicDTO inputTopicDTO = new TopicDTO(topicId, "newName", "newDescription", "newIcon");
        TopicModel requestModel = new TopicModel(topicId, "newName", "newDescription", "newIcon");
        TopicModel updatedTopic = new TopicModel(topicId, "newName", "newDescription", "newIcon");
        TopicDTO expectedTopic = TopicProcessorMapper.toDTO(updatedTopic);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", topicId);
        exchange.getIn().setBody(inputTopicDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with name " + updatedTopic.getName() + " updated successfully", expectedTopic);

        // mock
        when(topicService.updateTopic(topicId, requestModel)).thenReturn(updatedTopic);

        // when
        Exchange responseExchange = template.send(TopicRouteConstants.UPDATE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given invalid request when route updateTopic is called then throws ResourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteUpdateTopic_thenThrowsResourceNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;
        TopicDTO inputRequestDTO = new TopicDTO(nonExistingId, "newName", "newDescription", "newIcon");
        TopicModel request = new TopicModel(nonExistingId, "newName", "newDescription", "newIcon");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(inputRequestDTO);

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
    @DisplayName("given topic exists when route deleteTopic is called then topic deleted")
    void givenTopicExists_whenRouteDeleteTopic_thenTopicDeleted() {
        // given
        Long topicId = 1L;
        TopicModel topicModel = new TopicModel(topicId, "test", "Test", "Test topic");
        TopicDTO expectedTopic = new TopicDTO(topicId, "test", "Test", "Test topic");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", topicId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with id " + topicId + " deleted successfully", expectedTopic);

        // mock
        when(topicService.deleteTopicById(1L)).thenReturn(topicModel);

        // when
        Exchange result = template.send(TopicRouteConstants.DELETE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given invalid request when route deleteTopic is called then throws resourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteDeleteTopic_thenThrowsResourceNotFoundExceptionHandler() {
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
