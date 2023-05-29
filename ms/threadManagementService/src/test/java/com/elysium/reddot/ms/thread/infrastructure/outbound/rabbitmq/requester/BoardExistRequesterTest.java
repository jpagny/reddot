package com.elysium.reddot.ms.thread.infrastructure.outbound.rabbitmq.requester;

import com.elysium.reddot.ms.thread.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.thread.infrastructure.data.dto.BoardExistsResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardExistRequesterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private ObjectMapper objectMapper;

    private BoardExistRequester boardExistRequester;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        boardExistRequester = new BoardExistRequester(rabbitTemplate);
    }

    @Test
    @DisplayName("given a board id exists, when verifyBoardIdExistsOrThrow is called, then no exception is thrown")
    void givenBoardIdExists_whenVerifyBoardIdExistsOrThrow_thenNoException() throws Exception {
        // given
        Long boardId = 123L;
        BoardExistsResponseDTO response = new BoardExistsResponseDTO();
        response.setBoardId(boardId);
        response.setExists(true);

        // mock
        when(rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_BOARD_THREAD,
                RabbitMQConstant.REQUEST_BOARD_EXIST,
                boardId
        )).thenReturn(objectMapper.writeValueAsBytes(response));

        // when
        boardExistRequester.verifyBoardIdExistsOrThrow(boardId);

        // then
        // No exception is thrown
    }

    @Test
    @DisplayName("given a board id does not exist, when verifyBoardIdExistsOrThrow is called, then ResourceNotFoundException is thrown")
    void givenBoardIdDoesNotExist_whenVerifyBoardIdExistsOrThrow_thenException() throws IOException {
        // given
        Long boardId = 123L;
        BoardExistsResponseDTO response = new BoardExistsResponseDTO();
        response.setBoardId(boardId);
        response.setExists(false);

        // mock
        when(rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_BOARD_THREAD,
                RabbitMQConstant.REQUEST_BOARD_EXIST,
                boardId
        )).thenReturn(objectMapper.writeValueAsBytes(response));

        // when and then
        assertThrows(ResourceNotFoundException.class, () -> boardExistRequester.verifyBoardIdExistsOrThrow(boardId));
    }


}
