package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.topic.DeleteTopicProcessor;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTopicProcessorTest {

    private DeleteTopicProcessor deleteTopicProcessor;

    @Mock
    private TopicApplicationServiceImpl topicService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        deleteTopicProcessor = new DeleteTopicProcessor(topicService);
    }

    @Test
    @DisplayName("given valid topicId when deleteTopic is called then topic deleted Successfully")
    void givenValidTopicId_whenDeleteTopic_thenTopicIsDeletedSuccessfully() {
        // given
        Long topicId = 1L;
        TopicModel topicToDeleteModel = new TopicModel(topicId, "name", "Name", "Topic description");
        TopicDTO expectedTopic = TopicProcessorMapper.toDTO(topicToDeleteModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with id " + topicId + " deleted successfully", expectedTopic);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/topics/" + topicId);
        exchange.getIn().setHeader("id", topicId);

        // mock
        when(topicService.deleteTopicById(topicId)).thenReturn(topicToDeleteModel);

        // when
        deleteTopicProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());

        // verify
        verify(topicService).deleteTopicById(topicId);
    }

}
