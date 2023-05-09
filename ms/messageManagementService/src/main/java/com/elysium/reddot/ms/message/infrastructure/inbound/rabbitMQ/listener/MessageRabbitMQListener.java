package com.elysium.reddot.ms.message.infrastructure.inbound.rabbitMQ.listener;

import com.elysium.reddot.ms.message.application.service.MessageRabbitMQService;
import com.elysium.reddot.ms.message.infrastructure.data.MessageExistsResponseDTO;
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
public class MessageRabbitMQListener {

    private final RabbitTemplate rabbitTemplate;
    private final MessageRabbitMQService messageRabbitMQService;

    @RabbitListener(queues = "message.exists.queue")
    public void checkMessageExists(Message message) throws JsonProcessingException {
        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        Long messageId = (Long) messageConverter.fromMessage(message);

        boolean exists = messageRabbitMQService.checkMessageIdExists(messageId);
        MessageExistsResponseDTO response = new MessageExistsResponseDTO();
        response.setParentMessageID(messageId);
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

    }
}