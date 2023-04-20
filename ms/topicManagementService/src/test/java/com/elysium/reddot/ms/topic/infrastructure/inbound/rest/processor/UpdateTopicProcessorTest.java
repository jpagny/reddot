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
class UpdateTopicProcessorTest {

    private UpdateTopicProcessor updateTopicProcessor;

    @Mock
    private TopicApplicationServiceImpl topicService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        updateTopicProcessor = new UpdateTopicProcessor(topicService);
    }

    @Test
    @DisplayName("given valid topic when updateTopic is called then topic updated successfully")
    void givenValidTopic_whenUpdateTopic_thenTopicIsUpdatedSuccessfully() {
        // given
        Long topicId = 1L;
        TopicDTO updatedTopicDTO = new TopicDTO(topicId, "new_name", "New Name", "New Topic description");
        TopicModel updatedTopicModel = new TopicModel(topicId, "new_name", "New Name", "New Topic description");
        TopicDTO expectedTopic = TopicProcessorMapper.toDTO(updatedTopicModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with name " + updatedTopicModel.getName() + " updated successfully", expectedTopic);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/topics/" + topicId);
        exchange.getIn().setHeader("id", topicId);
        exchange.getIn().setBody(updatedTopicDTO);

        // mock
        when(topicService.updateTopic(topicId, updatedTopicModel)).thenReturn(updatedTopicModel);

        // when
        updateTopicProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }

}
