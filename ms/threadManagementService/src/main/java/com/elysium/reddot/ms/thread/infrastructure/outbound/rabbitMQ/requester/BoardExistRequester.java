package com.elysium.reddot.ms.thread.infrastructure.outbound.rabbitmq.requester;

import com.elysium.reddot.ms.thread.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.thread.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.thread.infrastructure.data.dto.BoardExistsResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The BoardExistRequester class is responsible for verifying the existence of a board ID by sending a request to RabbitMQ.
 * It uses a RabbitTemplate to send and receive messages and an ObjectMapper for JSON serialization/deserialization.
 */
@Component
@Slf4j
public class BoardExistRequester {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public BoardExistRequester(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Verifies the existence of a board ID or throws a ResourceNotFoundException if the board ID does not exist.
     *
     * @param boardId The ID of the board to verify.
     * @throws IOException                If there is an error in JSON serialization/deserialization.
     * @throws ResourceNotFoundException If the board ID does not exist.
     */
    public void verifyBoardIdExistsOrThrow(Long boardId) throws IOException {
        log.info("Verifying boardId existence for ID: {}", boardId);

        BoardExistsResponseDTO response = getBoardExistsResponse(boardId);

        if (!response.isExists()) {
            throw new ResourceNotFoundException("Board id", String.valueOf(boardId));
        }

        log.debug("Board id {} exists", boardId);
    }

    private BoardExistsResponseDTO getBoardExistsResponse(Long boardId) throws IOException {
        log.debug("Sending board existence request for ID: {}", boardId);

        Object replyObject = rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_BOARD_THREAD,
                RabbitMQConstant.REQUEST_BOARD_EXIST,
                boardId
        );

        assert replyObject != null;
        log.debug("Received response : " + replyObject);

        Map<?, ?> replyMap = (Map<?, ?>) replyObject;
        Map<String, Object> processedMap = replyMap.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue));

        BoardExistsResponseDTO responseDTO = objectMapper.convertValue(processedMap, BoardExistsResponseDTO.class);
        log.debug("Response in DTO : " + responseDTO.toString());
        return responseDTO;
    }
}