package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.service.ITopicApplicationServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeleteTopicProcessor implements Processor {

    private final ITopicApplicationServiceImpl topicService;

    @Override
    public void process(Exchange exchange) {
        Long id = exchange.getIn().getHeader("id", Long.class);
        TopicDTO topicDTO = topicService.getTopicById(id);

        topicService.deleteTopicById(id);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(), "Topic with id " + id + " deleted successfully", topicDTO);
        exchange.getMessage().setBody(apiResponseDTO);
    }
}