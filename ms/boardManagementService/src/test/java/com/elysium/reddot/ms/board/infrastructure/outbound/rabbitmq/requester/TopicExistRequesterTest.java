package com.elysium.reddot.ms.board.infrastructure.outbound.rabbitmq.requester;

import com.elysium.reddot.ms.board.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.board.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.board.infrastructure.data.dto.TopicExistsResponseDTO;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopicExistRequesterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private ObjectMapper objectMapper;

    private TopicExistRequester topicExistRequester;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        topicExistRequester = new TopicExistRequester(rabbitTemplate);
    }

    @Test
    @DisplayName("given a topic id exists, when verifyTopicIdExistsOrThrow is called, then no exception is thrown")
    void givenTopicIdExists_whenVerifyTopicIdExistsOrThrow_thenNoException() throws Exception {

        // given
        Long topicId = 123L;

        // mock
        when(rabbitTemplate.convertSendAndReceive(
                any(String.class),
                any(String.class),
                any(Long.class)
        )).thenReturn(Map.of("exists", true));


        // when
        assertDoesNotThrow(() -> topicExistRequester.verifyTopicIdExistsOrThrow(topicId));

        // then
        verify(rabbitTemplate).convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_TOPIC_BOARD,
                RabbitMQConstant.REQUEST_TOPIC_EXIST,
                topicId
        );

    }

    @Test
    @DisplayName("given a topic id does not exist, when verifyTopicIdExistsOrThrow is called, then ResourceNotFoundException is thrown")
    void givenTopicIdDoesNotExist_whenVerifyTopicIdExistsOrThrow_thenException() throws IOException {

        // given
        Long topicId = 123L;

        // mock
        when(rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_TOPIC_BOARD,
                RabbitMQConstant.REQUEST_TOPIC_EXIST,
                topicId
        )).thenReturn(Map.of("exists", false));

        // when and then
        assertThrows(ResourceNotFoundException.class, () -> topicExistRequester.verifyTopicIdExistsOrThrow(topicId));

    }


}
