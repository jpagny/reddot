package com.elysium.reddot.ms.board.infrastructure.outbound.rabbitmq.requester;

import com.elysium.reddot.ms.board.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.board.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.board.infrastructure.data.dto.TopicExistsResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Component class to request topic existence verification using RabbitMQ.
 */
@Component
@Slf4j
public class TopicExistRequester {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new TopicExistRequester.
     *
     * @param rabbitTemplate the RabbitTemplate for sending and receiving RabbitMQ messages
     */
    public TopicExistRequester(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Verifies if the topic with the given ID exists. Throws a ResourceNotFoundException if it doesn't.
     *
     * @param topicId the ID of the topic to verify
     * @throws IOException if an error occurs during JSON conversion
     */
    public void verifyTopicIdExistsOrThrow(Long topicId) throws IOException {
        log.info("Verifying topic existence for ID: {}", topicId);

        TopicExistsResponseDTO response = getTopicExistsResponse(topicId);

        if (!response.isExists()) {
            log.error("Topic id {} does not exist", topicId);
            throw new ResourceNotFoundException("Topic id", String.valueOf(topicId));
        }

        log.debug("Topic id {} exists", topicId);
    }

    private TopicExistsResponseDTO getTopicExistsResponse(Long topicId) throws IOException {
        log.debug("Sending topic existence request for ID: {}", topicId);

        Object replyObject = rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_TOPIC_BOARD,
                RabbitMQConstant.REQUEST_TOPIC_EXIST,
                topicId
        );

        assert replyObject != null;
        log.debug("Received response : " + replyObject);

        Map<?, ?> replyMap = (Map<?, ?>) replyObject;
        Map<String, Object> processedMap = replyMap.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue));

        TopicExistsResponseDTO responseDTO = objectMapper.convertValue(processedMap, TopicExistsResponseDTO.class);
        log.debug("Response in DTO : " + responseDTO.toString());
        return responseDTO;
    }
}