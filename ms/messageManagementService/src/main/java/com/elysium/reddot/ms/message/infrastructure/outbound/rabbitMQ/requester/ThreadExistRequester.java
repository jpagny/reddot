package com.elysium.reddot.ms.message.infrastructure.outbound.rabbitmq.requester;

import com.elysium.reddot.ms.message.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.message.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.message.infrastructure.data.rabbitmq.received.response.ThreadExistsResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

/**
 * The ThreadExistRequester class is responsible for verifying the existence of a thread
 * by sending a request and receiving a response through RabbitMQ.
 */
@Service
@Slf4j
public class ThreadExistRequester {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new ThreadExistRequester with the specified RabbitTemplate.
     *
     * @param rabbitTemplate the RabbitTemplate to use for sending and receiving messages
     */
    public ThreadExistRequester(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Verifies if a thread with the specified ID exists, and throws a ResourceNotFoundException if it does not.
     *
     * @param threadId the ID of the thread to verify
     * @throws IOException               if an I/O error occurs while sending or receiving the message
     * @throws ResourceNotFoundException if the thread with the specified ID does not exist
     */
    public void verifyThreadIdExistsOrThrow(Long threadId) throws IOException {
        ThreadExistsResponseDTO response = getMessageExistsResponse(threadId);

        if (!response.isExists()) {
            throw new ResourceNotFoundException("Thread", String.valueOf(threadId));
        }
    }

    private ThreadExistsResponseDTO getMessageExistsResponse(Long threadId) throws IOException {
        log.debug("Sending requester to check if threadId " + threadId + " is exist");

        byte[] replyBytes = (byte[]) rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_THREAD_MESSAGE,
                RabbitMQConstant.REQUEST_THREAD_EXIST,
                threadId
        );

        assert replyBytes != null;
        log.debug("Received response : " + Arrays.toString(replyBytes));

        try {
            ThreadExistsResponseDTO responseDTO = objectMapper.readValue(replyBytes, ThreadExistsResponseDTO.class);
            log.debug("Response in DTO : " + responseDTO.toString());
            return responseDTO;

        } catch (IOException ex) {
            log.error("Fail to convert to json : " + ex);
            throw new IOException("Failed to convert to json", ex);

        }
    }
}