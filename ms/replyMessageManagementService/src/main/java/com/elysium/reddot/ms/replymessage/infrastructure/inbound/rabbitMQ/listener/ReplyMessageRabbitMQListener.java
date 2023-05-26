package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rabbitmq.listener;

import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageRabbitMQService;
import com.elysium.reddot.ms.replymessage.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.replymessage.infrastructure.data.rabbitmq.received.request.CountRepliesMessageByUserBetweenTwoDatesRequest;
import com.elysium.reddot.ms.replymessage.infrastructure.data.rabbitmq.response.CountRepliesMessageByUserBetweenTwoDatesResponse;
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
public class ReplyMessageRabbitMQListener {

    private final RabbitTemplate rabbitTemplate;
    private final ReplyMessageRabbitMQService replyMessageRabbitMQService;

    @RabbitListener(queues = RabbitMQConstant.QUEUE_COUNT_REPLIES_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE)
    public void countMessagesByUserBetweenTwoDates(Message message) throws JsonProcessingException {
        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();

        String jsonMessage = (String) messageConverter.fromMessage(message);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        CountRepliesMessageByUserBetweenTwoDatesRequest requestReceived = objectMapper.readValue(jsonMessage, CountRepliesMessageByUserBetweenTwoDatesRequest.class);

        Integer countMessagesTotalOnRangeDate = replyMessageRabbitMQService.countRepliesMessageByUserIdBetweenTwoDates(
                requestReceived.getUserId(),
                requestReceived.getOnStart(),
                requestReceived.getOnEnd());
        CountRepliesMessageByUserBetweenTwoDatesResponse response = new CountRepliesMessageByUserBetweenTwoDatesResponse(countMessagesTotalOnRangeDate);

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