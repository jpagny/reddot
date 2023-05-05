package com.elysium.reddot.ms.thread.infrastructure.outbound.rabbitMQ.requester;

import com.elysium.reddot.ms.thread.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.thread.infrastructure.dto.BoardExistsResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class BoardExistRequester {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    public static final String BOARD_EXCHANGE = "boardExchange";
    public static final String BOARD_EXISTS_REQUEST_ROUTING_KEY = "board.exists.request";

    public BoardExistRequester(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void verifyBoardIdExistsOrThrow(Long threadId) {
        BoardExistsResponseDTO response = getThreadExistsResponse(threadId);

        if (response != null && !response.isExists()) {
            throw new ResourceNotFoundException("Thread id", String.valueOf(threadId));
        }
    }

    private BoardExistsResponseDTO getThreadExistsResponse(Long threadId) {
        byte[] replyBytes = (byte[]) rabbitTemplate.convertSendAndReceive(
                BOARD_EXCHANGE,
                BOARD_EXISTS_REQUEST_ROUTING_KEY,
                threadId
        );

        try {
            return objectMapper.readValue(replyBytes, BoardExistsResponseDTO.class);

        } catch (IOException ex) {
            log.error("Fail to convert to json : " + ex);
            throw new RuntimeException("Failed to convert to json", ex);

        }
    }
}