package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.topic;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.data.mapper.TopicDtoTopicModelMapper;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Component that handles the processing of creating a new topic.
 * It implements the Processor interface from Apache Camel.
 */
@Component
@AllArgsConstructor
@Slf4j
public class CreateTopicProcessor implements Processor {

    private final TopicApplicationServiceImpl topicApplicationService;

    @Override
    public void process(Exchange exchange) {
        log.debug("Processing create topic request...");

        TopicDTO inputTopicDTO = exchange.getIn().getBody(TopicDTO.class);
        TopicModel topicModel = TopicDtoTopicModelMapper.toModel(inputTopicDTO);

        TopicModel createdTopicModel = topicApplicationService.createTopic(topicModel);
        log.debug("Topic created successfully. ID: {}", createdTopicModel.getId());

        TopicDTO createdTopicDTO = TopicDtoTopicModelMapper.toDTO(createdTopicModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Topic with name " + createdTopicModel.getName() + " created successfully", createdTopicDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
        exchange.getMessage().setBody(apiResponseDTO);
        log.debug("Create topic request processed successfully.");
    }
}