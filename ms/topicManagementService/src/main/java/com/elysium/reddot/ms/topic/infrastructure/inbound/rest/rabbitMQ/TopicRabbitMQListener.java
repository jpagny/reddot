package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.rabbitmq;

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

@Component
@RequiredArgsConstructor
@Slf4j
public class TopicRabbitMQListener {

    private final RabbitTemplate rabbitTemplate;
    private final TopicRabbitMQService topicRabbitMQService;

    @RabbitListener(queues = RabbitMQConstant.QUEUE_TOPIC_EXIST)
    public void checkTopicExists(Message message) throws JsonProcessingException {
        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        Long topicId = (Long) messageConverter.fromMessage(message);

        boolean exists = topicRabbitMQService.checkTopicIdExists(topicId);

        log.debug("Check topic exists: {}", exists);

        TopicExistsResponseDTO response = new TopicExistsResponseDTO();
        response.setExists(exists);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReplyTo(message.getMessageProperties().getReplyTo());
        messageProperties.setCorrelationId(message.getMessageProperties().getCorrelationId());
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(response);

        Message responseMessage = new Message(jsonResponse.getBytes(), messageProperties);

        rabbitTemplate.send(
                message.getMessageProperties().getReplyTo(),
                responseMessage
        );

        log.debug("Sent response message: {}", responseMessage);
    }
}
