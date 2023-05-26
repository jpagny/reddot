package com.elysium.reddot.ms.thread.infrastructure.outbound.rabbitmq.requester;

import com.elysium.reddot.ms.thread.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.thread.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.thread.infrastructure.data.dto.BoardExistsResponseDTO;
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

    public BoardExistRequester(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void verifyBoardIdExistsOrThrow(Long boardId) throws IOException {
        BoardExistsResponseDTO response = getBoardExistsResponse(boardId);

        if (response != null && !response.isExists()) {
            throw new ResourceNotFoundException("Board id", String.valueOf(boardId));
        }
    }

    private BoardExistsResponseDTO getBoardExistsResponse(Long boardId) throws IOException {
        String jsonString = objectMapper.writeValueAsString(boardId);
        byte[] reply = (byte[]) rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_BOARD_THREAD,
                RabbitMQConstant.REQUEST_BOARD_EXIST,
                jsonString
        );

        try {
            return objectMapper.readValue(reply, BoardExistsResponseDTO.class);
        } catch (IllegalArgumentException ex) {
            log.error("Fail to convert to json : " + ex);
            throw new IOException("Failed to convert to json", ex);
        }
    }
}