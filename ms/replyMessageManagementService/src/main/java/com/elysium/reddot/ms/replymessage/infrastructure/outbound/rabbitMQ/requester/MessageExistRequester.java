package com.elysium.reddot.ms.replymessage.infrastructure.outbound.rabbitMQ.requester;

import com.elysium.reddot.ms.replymessage.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.replymessage.infrastructure.data.MessageExistsResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class MessageExistRequester {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    public static final String THREAD_MESSAGE_EXCHANGE = "messageReplyMessageExchange";
    public static final String THREAD_EXISTS_REQUEST_ROUTING_KEY = "message.exists.request";

    public MessageExistRequester(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void verifyMessageIdExistsOrThrow(Long messageId) {
        MessageExistsResponseDTO response = getMessageExistsResponse(messageId);

        if (response != null && !response.isExists()) {
            throw new ResourceNotFoundException("Message", String.valueOf(messageId));
        }
    }

    private MessageExistsResponseDTO getMessageExistsResponse(Long messageId) {
        byte[] replyBytes = (byte[]) rabbitTemplate.convertSendAndReceive(
                THREAD_MESSAGE_EXCHANGE,
                THREAD_EXISTS_REQUEST_ROUTING_KEY,
                messageId
        );

        try {
            return objectMapper.readValue(replyBytes, MessageExistsResponseDTO.class);

        } catch (IOException ex) {
            log.error("Fail to convert to json : " + ex);
            throw new RuntimeException("Failed to convert to json", ex);

        }
    }
}