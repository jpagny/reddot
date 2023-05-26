package com.elysium.reddot.ms.replymessage.infrastructure.outbound.rabbitmq.requester;

import com.elysium.reddot.ms.replymessage.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.replymessage.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.replymessage.infrastructure.data.rabbitmq.received.response.MessageExistsResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class MessageExistRequester {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public MessageExistRequester(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void verifyMessageIdExistsOrThrow(Long messageId) throws IOException {
        MessageExistsResponseDTO response = getMessageExistsResponse(messageId);

        if (response != null && !response.isExists()) {
            throw new ResourceNotFoundException("Message", String.valueOf(messageId));
        }
    }

    private MessageExistsResponseDTO getMessageExistsResponse(Long messageId) throws IOException {
        byte[] replyBytes = (byte[]) rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_MESSAGE_REPLYMESSAGE,
                RabbitMQConstant.REQUEST_MESSAGE_EXIST,
                messageId
        );

        try {
            return objectMapper.readValue(replyBytes, MessageExistsResponseDTO.class);

        } catch (IOException ex) {
            log.error("Fail to convert to json : " + ex);
            throw new IOException("Failed to convert to json", ex);

        }
    }
}