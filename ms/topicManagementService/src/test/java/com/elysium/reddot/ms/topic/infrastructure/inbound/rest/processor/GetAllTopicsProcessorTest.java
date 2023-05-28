package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.data.mapper.TopicDtoTopicModelMapper;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.topic.GetAllTopicsProcessor;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllTopicsProcessorTest {

    private GetAllTopicsProcessor getAllTopicsProcessor;

    @Mock
    private TopicApplicationServiceImpl topicService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        getAllTopicsProcessor = new GetAllTopicsProcessor(topicService);
    }

    @Test
    @DisplayName("given topics exist when get allTopics is called then all topics retrieved")
    void givenTopicsExist_whenGetAllTopics_thenAllTopicsAreRetrieved() {
        // given
        TopicModel topic1Model = new TopicModel(1L, "name 1", "Name 1", "Topic 1");
        TopicModel topic2Model = new TopicModel(2L, "name 2", "Name 2", "Topic 2");
        List<TopicModel> topicListModel = Arrays.asList(topic1Model, topic2Model);
        List<TopicDTO> expectedListTopics = TopicDtoTopicModelMapper.toDTOList(topicListModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All topics retrieved successfully", expectedListTopics);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/topics");

        // mock
        when(topicService.getAllTopics()).thenReturn(topicListModel);

        // when
        getAllTopicsProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }


}
