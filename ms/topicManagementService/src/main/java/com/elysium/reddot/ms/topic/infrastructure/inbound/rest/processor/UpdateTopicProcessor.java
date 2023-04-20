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
public class UpdateTopicProcessor implements Processor {

    private final TopicApplicationServiceImpl topicService;

    @Override
    public void process(Exchange exchange) {
        Long inputId = exchange.getIn().getHeader("id", Long.class);
        TopicDTO inputTopicDTO = exchange.getIn().getBody(TopicDTO.class);
        TopicModel topicToUpdateModel = TopicProcessorMapper.toModel(inputTopicDTO);

        TopicModel updatedTopicModel = topicService.updateTopic(inputId, topicToUpdateModel);

        TopicDTO updatedTopicDTO = TopicProcessorMapper.toDTO(updatedTopicModel);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Topic with name " + updatedTopicDTO.getName() + " updated successfully", updatedTopicDTO);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }
}
