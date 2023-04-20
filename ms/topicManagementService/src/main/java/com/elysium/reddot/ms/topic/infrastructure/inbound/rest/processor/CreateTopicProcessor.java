package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.infrastructure.mapper.TopicProcessorMapper;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateTopicProcessor implements Processor {

    private final TopicApplicationServiceImpl topicApplicationService;

    @Override
    public void process(Exchange exchange) {
        TopicDTO inputTopicDTO = exchange.getIn().getBody(TopicDTO.class);
        TopicModel topicModel = TopicProcessorMapper.toModel(inputTopicDTO);

        TopicModel createdTopicModel = topicApplicationService.createTopic(topicModel);

        TopicDTO createdTopicDTO = TopicProcessorMapper.toDTO(createdTopicModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Topic with name " + createdTopicModel.getName() + " created successfully", createdTopicDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }
}