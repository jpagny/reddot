package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.exception.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.topic.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.topic.application.exception.handler.core.ExceptionHandler;
import com.elysium.reddot.ms.topic.application.exception.handler.exceptionhandler.ResourceAlreadyExistExceptionHandler;
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

import java.util.ArrayList;
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
        List<ExceptionHandler> listExceptionHandlers = new ArrayList<>();
        ResourceAlreadyExistExceptionHandler existExceptionHandler = new ResourceAlreadyExistExceptionHandler();
        listExceptionHandlers.add(existExceptionHandler);
        CamelGlobalExceptionHandler camelGlobalExceptionHandler = new CamelGlobalExceptionHandler(listExceptionHandlers);

        // Create processor instances with the mocked topicService
        GetAllTopicsProcessor getAllTopicsProcessor = new GetAllTopicsProcessor(topicService);
        GetTopicByIdProcessor getTopicByIdProcessor = new GetTopicByIdProcessor(topicService);
        CreateTopicProcessor createTopicProcessor = new CreateTopicProcessor(topicService);
        UpdateTopicProcessor updateTopicProcessor = new UpdateTopicProcessor(topicService);
        DeleteTopicProcessor deleteTopicProcessor = new DeleteTopicProcessor(topicService);

        // Initialize TopicProcessorHolder with the processor instances
        TopicProcessorHolder topicProcessorHolder = new TopicProcessorHolder(
                getAllTopicsProcessor,
                getTopicByIdProcessor,
                createTopicProcessor,
                updateTopicProcessor,
                deleteTopicProcessor
        );

        return new TopicRouteBuilder(camelGlobalExceptionHandler, topicProcessorHolder);
    }

    @Test
    public void givenExistingTopic_whenGetTopicByIdIsCalledWithValidId_thenCorrectTopicIsReturned() {
        // given
        Long topicId = 1L;
        Exchange exchange = createExchangeWithBody(null);
        exchange.getIn().setHeader("id", topicId);

        // when
        Exchange responseExchange = template.send(TopicRouteConstants.GET_TOPIC_BY_ID.getRouteName(), exchange);
        ApiResponseDTO apiResponseDTO = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(HttpStatus.OK.value(), apiResponseDTO.getStatus());
        assertEquals("Topic with id " + topicId + " retrieved successfully", apiResponseDTO.getMessage());
    }

    @Test
    void givenTopicExists_whenCreatingSameTopic_thenResourceAlreadyExistExceptionHandlerIsTriggered() {
        // given
        TopicDTO existingTopic = new TopicDTO(1L, "name", "Name", "Topic description");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setBody(existingTopic);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // mock
        when(topicService.createTopic(existingTopic)).thenThrow(new ResourceAlreadyExistException("topic", "name", "name"));

        // when
        Exchange result = template.send("direct:createTopic", exchange);
        ApiResponseDTO apiResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(HttpStatus.CONFLICT.value(), apiResponse.getStatus());
        assertEquals("The topic with name 'name' already exists.", apiResponse.getMessage());
    }


}
