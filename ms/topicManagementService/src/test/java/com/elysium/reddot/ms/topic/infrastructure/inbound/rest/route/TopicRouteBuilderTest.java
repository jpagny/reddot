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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TopicRouteBuilderTest extends CamelTestSupport {

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
    public void givenTopicsExist_whenRouteGetAllTopicsIsCalled_thenAllTopicsAreRetrieved() {
        // given
        Exchange exchange = createExchangeWithBody(null);
        TopicDTO topic1 = new TopicDTO(1L, "name 1", "Name 1", "Topic 1");
        TopicDTO topic2 = new TopicDTO(2L, "name 2", "Name 2", "Topic 2");

        List<TopicDTO> topicList = Arrays.asList(topic1, topic2);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All topics retrieved successfully", topicList);

        // mock
        when(topicService.getAllTopics()).thenReturn(topicList);

        // when
        Exchange responseExchange = template.send("direct:getAllTopics", exchange);
        ApiResponseDTO actualApiResponse = responseExchange.getIn().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }


    @Test
    public void givenExistingTopic_whenRouteGetTopicByIdIsCalledWithValidId_thenCorrectTopicIsReturned() {
        // given
        Long topicId = 1L;
        Exchange exchange = createExchangeWithBody(null);
        exchange.getIn().setHeader("id", topicId);

        // when
        Exchange result = template.send(TopicRouteConstants.GET_TOPIC_BY_ID.getRouteName(), exchange);
        ApiResponseDTO apiResponseDTO = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(HttpStatus.OK.value(), apiResponseDTO.getStatus());
        assertEquals("Topic with id " + topicId + " retrieved successfully", apiResponseDTO.getMessage());
    }

    @Test
    public void givenNonExistingTopicId_whenGetTopicByIdIsCalled_thenThrowResourceNotFoundException() {
        // given
        Long nonExistingId = 99L;
        Exchange exchange = createExchangeWithBody(null);
        exchange.getIn().setHeader("id", nonExistingId);

        // mock
        when(topicService.getTopicById(nonExistingId)).thenThrow(new ResourceNotFoundException("topic", String.valueOf(nonExistingId)));

        // when
        Exchange result = template.send(TopicRouteConstants.GET_TOPIC_BY_ID.getRouteName(), exchange);
        ApiResponseDTO apiResponseDTO = result.getMessage().getBody(ApiResponseDTO.class);

        // assert
        assertEquals(HttpStatus.NOT_FOUND.value(), apiResponseDTO.getStatus());
        assertEquals("The topic with ID " + nonExistingId + " does not exist.", apiResponseDTO.getMessage());
    }


    @Test
    void givenTopicExists_whenRouteCreateTopicIsCalledWithCreatingSameTopic_thenResourceAlreadyExistExceptionHandlerIsTriggered() {
        // given
        TopicDTO existingTopic = new TopicDTO(1L, "name", "Name", "Topic description");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setBody(existingTopic);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // mock
        when(topicService.createTopic(existingTopic)).thenThrow(new ResourceAlreadyExistException("topic", "name", "name"));

        // when
        Exchange responseExchange = template.send("direct:createTopic", exchange);
        ApiResponseDTO apiResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(HttpStatus.CONFLICT.value(), apiResponse.getStatus());
        assertEquals("The topic with name 'name' already exists.", apiResponse.getMessage());
    }


}
