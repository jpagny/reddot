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

import java.util.Arrays;

@Component
@Slf4j
public class ThreadRabbitMQListener {

    private final RabbitTemplate rabbitTemplate;
    private final ThreadRabbitMQService threadRabbitMQService;
    private final ObjectMapper objectMapper;

    public ThreadRabbitMQListener(RabbitTemplate rabbitTemplate, ThreadRabbitMQService threadRabbitMQService) {
        this.rabbitTemplate = rabbitTemplate;
        this.threadRabbitMQService = threadRabbitMQService;
        this.objectMapper = new ObjectMapper();
    }

    @RabbitListener(queues = RabbitMQConstant.QUEUE_THREAD_EXIST)
    public void checkThreadExists(Message message) throws JsonProcessingException {
        log.debug("Received request");
        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        Long threadId = (Long) messageConverter.fromMessage(message);
        log.debug("Checking existing thread with id : " + threadId);

        boolean exists = threadRabbitMQService.checkThreadIdExists(threadId);
        log.debug("Result existing thread with id : " + exists);
        BoardExistsResponseDTO response = new BoardExistsResponseDTO();
        response.setExists(exists);

        log.debug("Building message");
        Message responseMessage = createResponseMessage(message, response);

        rabbitTemplate.send(
                message.getMessageProperties().getReplyTo(),
                responseMessage
        );
        log.debug("Sending the response with : " + Arrays.toString(message.getBody()));
    }

    private Message createResponseMessage(Message requestMessage, BoardExistsResponseDTO response) throws JsonProcessingException {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReplyTo(requestMessage.getMessageProperties().getReplyTo());
        messageProperties.setCorrelationId(requestMessage.getMessageProperties().getCorrelationId());
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);

        String jsonResponse = objectMapper.writeValueAsString(response);

        return new Message(jsonResponse.getBytes(), messageProperties);
    }
}
