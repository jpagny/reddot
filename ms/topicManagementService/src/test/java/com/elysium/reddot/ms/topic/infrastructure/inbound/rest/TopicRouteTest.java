package com.elysium.reddot.ms.topic.infrastructure.inbound.rest;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
import com.elysium.reddot.ms.topic.infrastructure.inbound.camel.GetAllTopicsProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
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
public class TopicRouteTest {

    private GetAllTopicsProcessor getAllTopicsProcessor;

    @Mock
    private TopicApplicationServiceImpl topicService;

    private CamelContext camelContext;

    @BeforeEach
    public void setUp() {
        camelContext = new DefaultCamelContext();
        getAllTopicsProcessor = new GetAllTopicsProcessor(topicService);
    }

    @Test
    public void givenTopicsExist_whenGetAllTopicsIsCalled_thenAllTopicsAreRetrieved() {
        // arrange
        TopicDTO topic1 = new TopicDTO(1L, "name 1", "Name 1", "Topic 1");
        TopicDTO topic2 = new TopicDTO(2L, "name 2", "Name 2", "Topic 2");
        List<TopicDTO> topicList = Arrays.asList(topic1, topic2);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All topics retrieved successfully", topicList);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/topics");

        // mock
        when(topicService.getAllTopics()).thenReturn(topicList);

        // act
        getAllTopicsProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // assert
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }


}
