package com.elysium.reddot.ms.board.infrastructure.inbound.rabbitMQ.listener;

import com.elysium.reddot.ms.board.application.service.BoardRabbitMQService;
import com.elysium.reddot.ms.board.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.board.infrastructure.data.dto.BoardExistsResponseDTO;
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
public class BoardRabbitMQListener {

    private final RabbitTemplate rabbitTemplate;
    private final BoardRabbitMQService boardRabbitMQService;

    @RabbitListener(queues = RabbitMQConstant.QUEUE_BOARD_EXIST)
    public void checkTopicExists(Message message) throws JsonProcessingException {
        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        Long boardId = (Long) messageConverter.fromMessage(message);

        boolean exists = boardRabbitMQService.checkBoardIdExists(boardId);
        BoardExistsResponseDTO response = new BoardExistsResponseDTO();
        response.setBoardId(boardId);
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