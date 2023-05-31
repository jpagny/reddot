package com.elysium.reddot.ms.message.outbound.rabbitmq.requester;

import com.elysium.reddot.ms.message.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.message.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.message.infrastructure.data.rabbitmq.received.response.ThreadExistsResponseDTO;
import com.elysium.reddot.ms.message.infrastructure.outbound.rabbitmq.requester.ThreadExistRequester;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThreadExistRequesterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private ObjectMapper objectMapper;

    private ThreadExistRequester threadExistRequester;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        threadExistRequester = new ThreadExistRequester(rabbitTemplate);
    }

    @Test
    @DisplayName("given a thread id exists, when verifyThreadIdExistsOrThrow is called, then no exception is thrown")
    void givenTopicIdExists_whenVerifyTopicIdExistsOrThrow_thenNoException() throws Exception {
        // given
        Long threadId = 123L;
        String threadIdString = "123";
        ThreadExistsResponseDTO response = new ThreadExistsResponseDTO();
        response.setExists(true);

        // mock
        when(rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_THREAD_MESSAGE,
                RabbitMQConstant.REQUEST_THREAD_EXIST,
                threadIdString
        )).thenReturn(objectMapper.writeValueAsBytes(response));

        // when
        threadExistRequester.verifyThreadIdExistsOrThrow(threadId);

        // then
        // No exception is thrown
    }

    @Test
    @DisplayName("given a thread id does not exist, when verifyThreadIdExistsOrThrow is called, then ResourceNotFoundException is thrown")
    void givenTopicIdDoesNotExist_whenVerifyTopicIdExistsOrThrow_thenException() throws IOException {
        // given
        Long threadId = 123L;
        String threadIdString = "123";
        ThreadExistsResponseDTO response = new ThreadExistsResponseDTO();
        response.setExists(false);

        // mock
        when(rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_THREAD_MESSAGE,
                RabbitMQConstant.REQUEST_THREAD_EXIST,
                threadIdString
        )).thenReturn(objectMapper.writeValueAsBytes(response));

        // when and then
        assertThrows(ResourceNotFoundException.class, () -> threadExistRequester.verifyThreadIdExistsOrThrow(threadId));
    }


}
