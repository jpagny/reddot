package com.elysium.reddot.ms.message.infrastructure.inbound.rabbitmq.listener;

import com.elysium.reddot.ms.message.application.service.MessageRabbitMQService;
import com.elysium.reddot.ms.message.infrastructure.data.rabbitMQ.received.request.CountMessageByUserBetweenTwoDatesRequest;
import com.elysium.reddot.ms.message.infrastructure.data.rabbitMQ.response.CountMessageByUserBetweenTwoDatesResponse;
import com.elysium.reddot.ms.message.infrastructure.data.rabbitMQ.response.MessageExistsResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

        MessageProperties messageProperties = buildMessageProperties(message);
        String jsonResponse = buildJsonResponse(response);
        Message responseMessage = buildMessageResponse(jsonResponse, messageProperties);

        sendResponseToRabbit(message, responseMessage);
    }

    @RabbitListener(queues = "count.message.user.dates.queue")
    public void countMessagesByUserBetweenTwoDates(Message message) throws JsonProcessingException {
        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();

        String jsonMessage = (String) messageConverter.fromMessage(message);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        CountMessageByUserBetweenTwoDatesRequest requestReceived = objectMapper.readValue(jsonMessage, CountMessageByUserBetweenTwoDatesRequest.class);

        Integer countMessagesTotalOnRangeDate = messageRabbitMQService.countMessageByUserIdBetweenTwoDates(
                requestReceived.getUserId(),
                requestReceived.getOnStart(),
                requestReceived.getOnEnd());
        CountMessageByUserBetweenTwoDatesResponse response = new CountMessageByUserBetweenTwoDatesResponse(countMessagesTotalOnRangeDate);
        MessageProperties messageProperties = buildMessageProperties(message);
        String jsonResponse = buildJsonResponse(response);
        Message responseMessage = buildMessageResponse(jsonResponse, messageProperties);
        sendResponseToRabbit(message, responseMessage);
    }

    private MessageProperties buildMessageProperties(Message message) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReplyTo(message.getMessageProperties().getReplyTo());
        messageProperties.setCorrelationId(message.getMessageProperties().getCorrelationId());
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
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
        rabbitTemplate.send(
                message.getMessageProperties().getReplyTo(),
                responseMessage
        );
    }


}