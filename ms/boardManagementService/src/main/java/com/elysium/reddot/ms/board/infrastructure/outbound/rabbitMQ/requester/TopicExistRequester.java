package com.elysium.reddot.ms.board.infrastructure.outbound.rabbitMQ.requester;

import com.elysium.reddot.ms.board.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.board.infrastructure.data.dto.TopicExistsResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class TopicExistRequester {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String TOPIC_EXISTS_REQUEST_ROUTING_KEY = "topic.exists.request";

    public TopicExistRequester(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void verifyTopicIdExistsOrThrow(Long topicId) {
        TopicExistsResponseDTO response = getTopicExistsResponse(topicId);

        if (response != null && !response.isExists()) {
            throw new ResourceNotFoundException("Topic id", String.valueOf(topicId));
        }
    }

    private TopicExistsResponseDTO getTopicExistsResponse(Long topicId) {
        byte[] replyBytes = (byte[]) rabbitTemplate.convertSendAndReceive(
                TOPIC_EXCHANGE,
                TOPIC_EXISTS_REQUEST_ROUTING_KEY,
                topicId
        );

        try {
            return objectMapper.readValue(replyBytes, TopicExistsResponseDTO.class);

        } catch (IOException ex) {
            log.error("Fail to convert to json : " + ex);
            throw new RuntimeException("Failed to convert to json", ex);

        }
    }
}