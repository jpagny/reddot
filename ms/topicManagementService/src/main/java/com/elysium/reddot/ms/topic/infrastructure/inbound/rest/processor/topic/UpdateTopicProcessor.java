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
 * Component that handles the processing of updating a topic.
 * It implements the Processor interface from Apache Camel.
 */
@Component
@AllArgsConstructor
@Slf4j
public class UpdateTopicProcessor implements Processor {

    private final TopicApplicationServiceImpl topicService;

    @Override
    public void process(Exchange exchange) {
        log.debug("Processing update topic request...");

        Long inputId = exchange.getIn().getHeader("id", Long.class);
        TopicDTO inputTopicDTO = exchange.getIn().getBody(TopicDTO.class);
        TopicModel topicToUpdateModel = TopicDtoTopicModelMapper.toModel(inputTopicDTO);

        TopicModel updatedTopicModel = topicService.updateTopic(inputId, topicToUpdateModel);
        log.debug("Updating topic with ID: {}", inputId);

        TopicDTO updatedTopicDTO = TopicDtoTopicModelMapper.toDTO(updatedTopicModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Topic with name " + updatedTopicDTO.getName() + " updated successfully", updatedTopicDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);
        log.debug("Update topic request processed successfully.");
    }
}
