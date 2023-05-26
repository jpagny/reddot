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
        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        Long threadId = (Long) messageConverter.fromMessage(message);

        boolean exists = threadRabbitMQService.checkThreadIdExists(threadId);
        BoardExistsResponseDTO response = new BoardExistsResponseDTO();
        response.setExists(exists);

        Message responseMessage = createResponseMessage(message, response);

        rabbitTemplate.send(
                message.getMessageProperties().getReplyTo(),
                responseMessage
        );
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
