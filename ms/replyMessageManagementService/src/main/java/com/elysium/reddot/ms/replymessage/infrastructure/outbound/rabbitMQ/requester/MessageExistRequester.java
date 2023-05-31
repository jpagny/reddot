package com.elysium.reddot.ms.replymessage.infrastructure.outbound.rabbitmq.requester;

import com.elysium.reddot.ms.replymessage.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.replymessage.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.replymessage.infrastructure.data.rabbitmq.received.response.MessageExistsResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

/**
 * The MessageExistRequester class is responsible for requesting the existence of a message from RabbitMQ.
 * It sends a request message to the message/reply_message exchange and waits for a response.
 */
@Component
@Slf4j
public class MessageExistRequester {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public MessageExistRequester(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Verifies if a message with the given ID exists in the system. If the message does not exist, a ResourceNotFoundException is thrown.
     *
     * @param messageId the ID of the message to verify
     * @throws IOException               if there is an error while processing the response message
     * @throws ResourceNotFoundException if the message with the given ID does not exist
     */
    public void verifyMessageIdExistsOrThrow(Long messageId) throws IOException {
        MessageExistsResponseDTO response = getMessageExistsResponse(messageId);

        if (!response.isExists()) {
            throw new ResourceNotFoundException("Message", String.valueOf(messageId));
        }
    }

    private MessageExistsResponseDTO getMessageExistsResponse(Long messageId) throws IOException {
        log.debug("Sending requester to check if messageId " + messageId + " is exist");

        byte[] replyBytes = (byte[]) rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_MESSAGE_REPLYMESSAGE,
                RabbitMQConstant.REQUEST_MESSAGE_EXIST,
                String.valueOf(messageId)
        );

        assert replyBytes != null;
        log.debug("Received response : " + Arrays.toString(replyBytes));

        try {
            MessageExistsResponseDTO responseDTO = objectMapper.readValue(replyBytes, MessageExistsResponseDTO.class);
            log.debug("Response in DTO : " + responseDTO.toString());
            return responseDTO;

        } catch (IOException ex) {
            log.error("Fail to convert to json : " + ex);
            throw new IOException("Failed to convert to json", ex);

        }
    }
}