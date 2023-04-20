package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.infrastructure.mapper.TopicProcessorMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTopicByIdProcessorTest {

    private GetTopicByIdProcessor getTopicByIdProcessor;

    @Mock
    private TopicApplicationServiceImpl topicService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        getTopicByIdProcessor = new GetTopicByIdProcessor(topicService);
    }

    @Test
    @DisplayName("given topic exists when getTopicById is called then topic retrieved")
    void givenTopicExists_whenGetTopicById_thenTopicIsRetrieved() {
        // given
        Long id = 1L;
        TopicModel topicModel = new TopicModel(id, "name 1", "Name 1", "Topic 1");
        TopicDTO expectedTopic = TopicProcessorMapper.toDTO(topicModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with id " + id + " retrieved successfully", expectedTopic);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/topics/" + id);
        exchange.getIn().setHeader("id", id);

        // mock
        when(topicService.getTopicById(id)).thenReturn(topicModel);

        // when
        getTopicByIdProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }

}
