package com.elysium.reddot.ms.board.application.service;

import com.elysium.reddot.ms.board.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.board.infrastructure.data.dto.TopicExistsResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQService {

    private final RabbitTemplate rabbitTemplate;
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String TOPIC_EXISTS_REQUEST_ROUTING_KEY = "topic.exists.request";

    public void verifyTopicIdExistsOrThrow(Long topicId) {

        byte[] replyBytes;
        ObjectMapper objectMapper = new ObjectMapper();
        TopicExistsResponseDTO response;

        try {
            replyBytes = (byte[]) rabbitTemplate.convertSendAndReceive(
                    TOPIC_EXCHANGE,
                    TOPIC_EXISTS_REQUEST_ROUTING_KEY,
                    topicId
            );

            response = objectMapper.readValue(replyBytes, TopicExistsResponseDTO.class);

        } catch (IOException ex) {
            log.error("Fail to convert to json : " + ex);
            throw new RuntimeException("Failed to convert to json", ex);

        }

        if (response != null && !response.isExists()) {
            throw new ResourceNotFoundException("Topic id", String.valueOf(topicId));
        }

    }
}
