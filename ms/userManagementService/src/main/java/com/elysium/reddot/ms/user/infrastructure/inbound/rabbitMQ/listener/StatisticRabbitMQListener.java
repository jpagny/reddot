package com.elysium.reddot.ms.user.infrastructure.inbound.rabbitMQ.listener;

import com.elysium.reddot.ms.user.application.service.UserRabbitMQService;
import com.elysium.reddot.ms.user.infrastructure.data.rabbitMQ.response.ListUserIdsResponse;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class StatisticRabbitMQListener {

    private final RabbitTemplate rabbitTemplate;
    private final UserRabbitMQService userRabbitMQService;

    @RabbitListener(queues = "user.all.queue")
    public void getAllUserIds(Message message) throws JsonProcessingException {
        ArrayList<String> listUserIds = userRabbitMQService.getAllUsers();

        ListUserIdsResponse response = new ListUserIdsResponse(listUserIds);

        MessageProperties messageProperties = buildMessageProperties(message);
        String jsonResponse = buildJsonResponse(response);
        log.debug("RESULT JSON : " + jsonResponse);
        Message responseMessage = buildMessageResponse(jsonResponse, messageProperties);
        log.debug(responseMessage.toString());
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
        log.debug("VERIFY JSON : " + jsonResponse);
        return new Message(jsonResponse.getBytes(), messageProperties);
    }

    private void sendResponseToRabbit(Message message, Message responseMessage) {
        rabbitTemplate.send(
                message.getMessageProperties().getReplyTo(),
                responseMessage
        );
    }


}