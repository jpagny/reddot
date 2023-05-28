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

import java.util.List;

/**
 * Component that handles the processing of retrieving all topics.
 * It implements the Processor interface from Apache Camel.
 */
@Component
@AllArgsConstructor
@Slf4j
public class GetAllTopicsProcessor implements Processor {

    private final TopicApplicationServiceImpl topicService;

    @Override
    public void process(Exchange exchange) {
        log.debug("Processing get all topics request...");

        List<TopicModel> listTopicsModel = topicService.getAllTopics();

        log.debug("Retrieved {} topics", listTopicsModel.size());

        List<TopicDTO> listTopicsDTO = TopicDtoTopicModelMapper.toDTOList(listTopicsModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "All topics retrieved successfully", listTopicsDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);
        log.debug("Get all topics request processed successfully.");
    }
}
