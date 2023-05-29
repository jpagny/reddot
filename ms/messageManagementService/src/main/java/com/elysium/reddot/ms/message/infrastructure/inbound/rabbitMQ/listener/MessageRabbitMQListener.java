package com.elysium.reddot.ms.message.infrastructure.inbound.rabbitmq.listener;

import com.elysium.reddot.ms.message.application.service.MessageRabbitMQService;
import com.elysium.reddot.ms.message.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.message.infrastructure.data.rabbitmq.received.request.CountMessageByUserBetweenTwoDatesRequest;
import com.elysium.reddot.ms.message.infrastructure.data.rabbitmq.response.CountMessageByUserBetweenTwoDatesResponse;
import com.elysium.reddot.ms.message.infrastructure.data.rabbitmq.response.MessageExistsResponseDTO;
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

/**
 * A component that listens to RabbitMQ messages related to messages and performs corresponding actions.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MessageRabbitMQListener {

    private final RabbitTemplate rabbitTemplate;
    private final MessageRabbitMQService messageRabbitMQService;

    /**
     * Listens to the QUEUE_MESSAGE_EXIST queue and checks the existence of a message.
     *
     * @param message the incoming RabbitMQ message
     * @throws JsonProcessingException if an error occurs during JSON processing
     */
    @RabbitListener(queues = RabbitMQConstant.QUEUE_MESSAGE_EXIST)
    public void checkMessageExists(Message message) throws JsonProcessingException {
        log.debug("Received RabbitMQ message to check message existence.");

        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        Long messageId = (Long) messageConverter.fromMessage(message);
        log.debug("Received message ID: {}", messageId);

        boolean exists = messageRabbitMQService.checkMessageIdExists(messageId);
        log.debug("Message existence check result: {}", exists);

        MessageExistsResponseDTO response = new MessageExistsResponseDTO();
        response.setExists(exists);
        log.debug("Created MessageExistsResponseDTO: {}", response);

        MessageProperties messageProperties = buildMessageProperties(message);
        String jsonResponse = buildJsonResponse(response);
        Message responseMessage = buildMessageResponse(jsonResponse, messageProperties);
        log.debug("Built response message: {}", responseMessage);

        sendResponseToRabbit(message, responseMessage);
        log.debug("Sent response for message existence check.");
    }

    /**
     * Listens to the QUEUE_COUNT_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE queue and counts the number of messages
     * by user within a specified date range.
     *
     * @param message the incoming RabbitMQ message
     * @throws JsonProcessingException if an error occurs during JSON processing
     */
    @RabbitListener(queues = RabbitMQConstant.QUEUE_COUNT_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE)
    public void countMessagesByUserBetweenTwoDates(Message message) throws JsonProcessingException {
        log.debug("Received RabbitMQ message to count messages by user between two dates.");

        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();

        String jsonMessage = (String) messageConverter.fromMessage(message);
        log.debug("Received JSON message: {}", jsonMessage);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        CountMessageByUserBetweenTwoDatesRequest requestReceived = objectMapper.readValue(jsonMessage, CountMessageByUserBetweenTwoDatesRequest.class);
        log.debug("Parsed CountMessageByUserBetweenTwoDatesRequest: {}", requestReceived);

        Integer countMessagesTotalOnRangeDate = messageRabbitMQService.countMessageByUserIdBetweenTwoDates(
                requestReceived.getUserId(),
                requestReceived.getOnStart(),
                requestReceived.getOnEnd());
        log.debug("Total count of messages for user between dates: {}", countMessagesTotalOnRangeDate);

        CountMessageByUserBetweenTwoDatesResponse response = new CountMessageByUserBetweenTwoDatesResponse(countMessagesTotalOnRangeDate);
        log.debug("Created CountMessageByUserBetweenTwoDatesResponse: {}", response);

        MessageProperties messageProperties = buildMessageProperties(message);
        String jsonResponse = buildJsonResponse(response);
        Message responseMessage = buildMessageResponse(jsonResponse, messageProperties);
        log.debug("Built response message: {}", responseMessage);

        sendResponseToRabbit(message, responseMessage);

        log.debug("Sent response for counting messages by user between two dates.");
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