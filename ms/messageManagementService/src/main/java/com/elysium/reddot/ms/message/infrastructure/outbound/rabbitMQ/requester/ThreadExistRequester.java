package com.elysium.reddot.ms.message.infrastructure.outbound.rabbitmq.requester;

import com.elysium.reddot.ms.message.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.message.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.message.infrastructure.data.rabbitMQ.received.response.ThreadExistsResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class ThreadExistRequester {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public ThreadExistRequester(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void verifyThreadIdExistsOrThrow(Long messageId) {
        ThreadExistsResponseDTO response = getMessageExistsResponse(messageId);

        if (response != null && !response.isExists()) {
            throw new ResourceNotFoundException("Thread", String.valueOf(messageId));
        }
    }

    private ThreadExistsResponseDTO getMessageExistsResponse(Long messageId) {
        byte[] replyBytes = (byte[]) rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_THREAD_MESSAGE,
                RabbitMQConstant.REQUEST_THREAD_EXIST,
                messageId
        );

        try {
            return objectMapper.readValue(replyBytes, ThreadExistsResponseDTO.class);

        } catch (IOException ex) {
            log.error("Fail to convert to json : " + ex);
            throw new RuntimeException("Failed to convert to json", ex);

        }
    }
}