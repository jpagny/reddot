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
public class GetTopicByIdProcessor implements Processor {

    private final ITopicApplicationServiceImpl topicService;

    @Override
    public void process(Exchange exchange) {
        Long id = Long.parseLong(exchange.getIn().getHeader("id").toString());
        TopicDTO topic = topicService.getTopicById(id);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "Topic with id " + id + " retrieved successfully", topic);
        exchange.getMessage().setBody(apiResponseDTO);
    }

}