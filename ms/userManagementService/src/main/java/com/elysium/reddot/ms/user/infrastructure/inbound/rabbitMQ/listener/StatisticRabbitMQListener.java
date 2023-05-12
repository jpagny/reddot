package com.elysium.reddot.ms.user.infrastructure.inbound.rabbitMQ.listener;

import com.elysium.reddot.ms.user.application.service.UserRabbitMQService;
import com.elysium.reddot.ms.user.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.user.infrastructure.data.rabbitMQ.send.response.ListUserIdsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Listener for processing statistic messages received from RabbitMQ.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StatisticRabbitMQListener {

    private final RabbitTemplate rabbitTemplate;
    private final UserRabbitMQService userRabbitMQService;

    /**
     * Retrieves all user IDs and sends the response via RabbitMQ.
     *
     * @param message the incoming message triggering the retrieval of user IDs
     * @throws JsonProcessingException if an error occurs during JSON processing
     */
    @RabbitListener(queues = RabbitMQConstant.QUEUE_FETCH_ALL_USERS)
    public void getAllUserIds(Message message) throws JsonProcessingException {
        log.debug("Received message: {}", message);

        ArrayList<String> listUserIds = userRabbitMQService.getAllUsers();

        log.debug("Retrieved user IDs: {}", listUserIds);

        ListUserIdsResponse response = new ListUserIdsResponse(listUserIds);

        MessageProperties messageProperties = buildMessageProperties(message);
        String jsonResponse = buildJsonResponse(response);
        Message responseMessage = buildMessageResponse(jsonResponse, messageProperties);
        sendResponseToRabbit(message, responseMessage);

        log.debug("Sent response message: {}", responseMessage);
    }

    /**
     * Builds the message properties for the response message based on the original message.
     *
     * @param message the original message containing the replyTo and correlationId properties
     * @return the message properties for the response message
     */
    private MessageProperties buildMessageProperties(Message message) {
        log.debug("Building message properties");

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReplyTo(message.getMessageProperties().getReplyTo());
        messageProperties.setCorrelationId(message.getMessageProperties().getCorrelationId());
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);

        log.debug("Message properties built: {}", messageProperties);

        return messageProperties;
    }

    /**
     * Builds a JSON response string from the provided response object.
     *
     * @param response the response object to be converted to JSON
     * @return the JSON response as a string
     * @throws JsonProcessingException if an error occurs during JSON processing
     */
    private String buildJsonResponse(Object response) throws JsonProcessingException {
        log.debug("Building JSON response");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(response);

        log.debug("JSON response built: {}", jsonResponse);
        return jsonResponse;
    }

    /**
     * Builds a response Message object with the provided JSON response and message properties.
     *
     * @param jsonResponse      the JSON response as a String
     * @param messageProperties the message properties for the response
     * @return the response Message object
     */
    private Message buildMessageResponse(String jsonResponse, MessageProperties messageProperties) {
        log.debug("Building response message");

        Message responseMessage = new Message(jsonResponse.getBytes(), messageProperties);

        log.debug("Response message built: {}", responseMessage);
        return responseMessage;
    }

    /**
     * Sends the response message to RabbitMQ.
     *
     * @param message         the original message containing the replyTo destination
     * @param responseMessage the response message to be sent
     */
    private void sendResponseToRabbit(Message message, Message responseMessage) {
        log.debug("Sending response message to RabbitMQ");
        log.debug("ReplyTo: {}", message.getMessageProperties().getReplyTo());
        log.debug("Response Message: {}", responseMessage);

        rabbitTemplate.send(
                message.getMessageProperties().getReplyTo(),
                responseMessage
        );

        log.debug("Response message sent to RabbitMQ");
    }


}