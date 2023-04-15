package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
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
        Long id = exchange.getIn().getHeader("id", Long.class);
        TopicDTO topicDto = exchange.getIn().getBody(TopicDTO.class);

        TopicDTO updatedTopic = topicService.updateTopic(id, topicDto);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Topic with name " + updatedTopic.getName() + " updated successfully", updatedTopic);
        exchange.getMessage().setBody(apiResponseDTO);
    }
}
