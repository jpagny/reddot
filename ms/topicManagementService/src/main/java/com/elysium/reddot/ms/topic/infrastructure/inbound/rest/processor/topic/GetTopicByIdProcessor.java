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
 * Component that handles the processing of retrieving a topic by ID.
 * It implements the Processor interface from Apache Camel.
 */
@Component
@AllArgsConstructor
@Slf4j
public class GetTopicByIdProcessor implements Processor {

    private final TopicApplicationServiceImpl topicService;

    @Override
    public void process(Exchange exchange) {
        log.debug("Processing get topic by ID request...");

        Long inputId = Long.parseLong(exchange.getIn().getHeader("id").toString());

        TopicModel topicModel = topicService.getTopicById(inputId);
        log.debug("Retrieving topic with ID: {}", inputId);

        TopicDTO topicDTO = TopicDtoTopicModelMapper.toDTO(topicModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with id " + inputId + " retrieved successfully", topicDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);
        log.debug("Get topic by ID request processed successfully.");
    }

}