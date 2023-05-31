package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.data.mapper.TopicDtoTopicModelMapper;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.topic.CreateTopicProcessor;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTopicProcessorTest {

    private CreateTopicProcessor createTopicProcessor;

    @Mock
    private TopicApplicationServiceImpl topicService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        createTopicProcessor = new CreateTopicProcessor(topicService);
    }

    @Test
    @DisplayName("given valid topic when createTopic is called then topic created successfully")
    void givenValidTopic_whenCreateTopic_thenTopicCreatedSuccessfully() {
        // given
        TopicDTO topicToCreateDTO = new TopicDTO(null, "name", "Name", "Topic description");
        TopicModel topicToCreateModel = new TopicModel(null, "name", "Name", "Topic description");
        TopicModel createdTopicModel = new TopicModel(1L, "name", "Name", "Topic description");
        TopicDTO expectedTopic = TopicDtoTopicModelMapper.toDTO(createdTopicModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Topic with name " + createdTopicModel.getName() + " created successfully", expectedTopic);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/topics");
        exchange.getIn().setBody(topicToCreateDTO);

        // mock
        when(topicService.createTopic(topicToCreateModel)).thenReturn(createdTopicModel);

        // when
        createTopicProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }


}
