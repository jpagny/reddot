package com.elysium.reddot.ms.topic.infrastructure.inbound.rabbitmq;

import com.elysium.reddot.ms.topic.application.service.TopicRabbitMQService;
import com.elysium.reddot.ms.topic.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.topic.infrastructure.data.dto.TopicExistsResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Component that listens to RabbitMQ messages related to topics.
 * It handles the checking of topic existence and sends a response back to the sender.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TopicRabbitMQListener {

    private final RabbitTemplate rabbitTemplate;
    private final TopicRabbitMQService topicRabbitMQService;
    private final ObjectMapper objectMapper;

    /**
     * Listens to the QUEUE_TOPIC_EXIST queue and checks if a topic with the given ID exists.
     * Sends a reply to the 'ReplyTo' header with the result of the check.
     *
     * @param message the incoming RabbitMQ message
     * @throws JsonProcessingException if any error occurs during JSON processing
     */
    @RabbitListener(queues = RabbitMQConstant.QUEUE_TOPIC_EXIST)
    public void checkTopicExists(Message message) throws IOException {
        log.debug("Received RabbitMQ message to check topic existence.");

        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        byte[] extractedMessage = (byte[]) messageConverter.fromMessage(message);
        Long topicId = objectMapper.readValue(extractedMessage, Long.class);

        log.debug("Checking existence of topic with ID: {}", topicId);

        boolean exists = topicRabbitMQService.checkTopicIdExists(topicId);

        log.debug("Topic existence check result: {}", exists);

        TopicExistsResponseDTO response = new TopicExistsResponseDTO();
        response.setExists(exists);

        MessageProperties messageProperties = buildMessageProperties(message);
        String jsonResponse = buildJsonResponse(response);
        Message responseMessage = buildMessageResponse(jsonResponse, messageProperties);

        sendResponseToRabbit(message, responseMessage);
        log.debug("Sent response for topic existence check.");
    }

    private MessageProperties buildMessageProperties(Message message) {
        log.debug("Building message properties for response message.");

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReplyTo(message.getMessageProperties().getReplyTo());
        messageProperties.setCorrelationId(message.getMessageProperties().getCorrelationId());
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);

        log.debug("Message properties built: {}", messageProperties);

        return messageProperties;
    }

    private String buildJsonResponse(Object response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(response);
    }

    private Message buildMessageResponse(String jsonResponse, MessageProperties messageProperties) {
        return new Message(jsonResponse.getBytes(), messageProperties);
    }

    private void sendResponseToRabbit(Message message, Message responseMessage) {
        log.debug("Sending response to RabbitMQ.");

        rabbitTemplate.send(
                message.getMessageProperties().getReplyTo(),
                responseMessage
        );
    }
}
