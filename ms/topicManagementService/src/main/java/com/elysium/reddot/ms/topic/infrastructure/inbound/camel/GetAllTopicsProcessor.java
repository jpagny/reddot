package com.elysium.reddot.ms.topic.infrastructure.inbound.camel;

import com.elysium.reddot.ms.topic.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GetAllTopicsProcessor implements Processor {

    private final TopicApplicationServiceImpl topicService;

    @Override
    public void process(Exchange exchange) {
        List<TopicDTO> topics = topicService.getAllTopics();
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.OK.value(),
                "All topics retrieved successfully", topics);
        exchange.getMessage().setBody(apiResponseDTO);
    }
}
