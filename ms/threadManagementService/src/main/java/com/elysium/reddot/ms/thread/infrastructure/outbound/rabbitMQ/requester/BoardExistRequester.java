package com.elysium.reddot.ms.thread.infrastructure.outbound.rabbitmq.requester;

import com.elysium.reddot.ms.thread.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.thread.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.thread.infrastructure.data.dto.BoardExistsResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

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
        log.debug("Sending requester to check if boardId " + boardId + " is exist");

        String jsonString = objectMapper.writeValueAsString(boardId);
        byte[] reply = (byte[]) rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_BOARD_THREAD,
                RabbitMQConstant.REQUEST_BOARD_EXIST,
                jsonString
        );

        assert reply != null;
        log.debug("Received response : " + Arrays.toString(reply));

        try {
            BoardExistsResponseDTO responseDTO = objectMapper.readValue(reply, BoardExistsResponseDTO.class);
            log.debug("Response in DTO : " + responseDTO.toString());
            return responseDTO;

        } catch (IllegalArgumentException ex) {
            log.error("Fail to convert to json : " + ex);
            throw new IOException("Failed to convert to json", ex);

        }
    }
}