package com.elysium.reddot.ms.thread.infrastructure.inbound.rabbitmq.listener;

import com.elysium.reddot.ms.thread.application.service.ThreadRabbitMQService;
import com.elysium.reddot.ms.thread.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.thread.infrastructure.data.dto.BoardExistsResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * Listener class to handle messages from RabbitMQ related to thread existence checks.
 */
@Component
@Slf4j
public class ThreadRabbitMQListener {

    private final RabbitTemplate rabbitTemplate;
    private final ThreadRabbitMQService threadRabbitMQService;

    public ThreadRabbitMQListener(RabbitTemplate rabbitTemplate, ThreadRabbitMQService threadRabbitMQService) {
        this.rabbitTemplate = rabbitTemplate;
        this.threadRabbitMQService = threadRabbitMQService;
    }

    /**
     * Listens to the QUEUE_THREAD_EXIST queue and checks if a thread with the given ID exists.
     * Sends a reply to the 'ReplyTo' header with the result of the check.
     *
     * @param message the incoming RabbitMQ message
     * @throws JsonProcessingException if any error occurs during JSON processing
     */
    @RabbitListener(queues = RabbitMQConstant.QUEUE_THREAD_EXIST)
    public void checkThreadExists(Message message) throws JsonProcessingException {
        log.debug("Received RabbitMQ message to check thread existence.");

        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        Object rawMessage = messageConverter.fromMessage(message);

        byte[] byteArray = (byte[]) rawMessage;
        String rawString = new String(byteArray, StandardCharsets.UTF_8);
        log.debug("Raw byte array as string: {}", rawString);

        Long threadId = Long.parseLong(rawString);

        log.debug("Checking existence of thread with ID: {}", threadId);

        boolean exists = threadRabbitMQService.checkThreadIdExists(threadId);

        log.debug("Board existence check result: {}", exists);

        BoardExistsResponseDTO response = new BoardExistsResponseDTO();
        response.setExists(exists);

        MessageProperties messageProperties = buildMessageProperties(message);
        String jsonResponse = buildJsonResponse(response);
        Message responseMessage = buildMessageResponse(jsonResponse, messageProperties);

        sendResponseToRabbit(message, responseMessage);
        log.debug("Sent response for thread existence check.");
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
