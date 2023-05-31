package com.elysium.reddot.ms.replymessage.infrastructure.outbound.rabbitmq.requester;

import com.elysium.reddot.ms.replymessage.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.replymessage.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.replymessage.infrastructure.data.rabbitmq.response.MessageExistsResponseDTO;
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
class MessageExistRequesterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private ObjectMapper objectMapper;

    private MessageExistRequester messageExistRequester;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        messageExistRequester = new MessageExistRequester(rabbitTemplate);
    }

    @Test
    @DisplayName("given a message id exists, when verifyMessageIdExistsOrThrow is called, then no exception is thrown")
    void givenMessageIdExists_whenVerifyMessageIdExistsOrThrow_thenNoException() throws Exception {
        // given
        Long parentMessageId = 123L;
        String parentMessageIdString = "123";
        MessageExistsResponseDTO response = new MessageExistsResponseDTO();
        response.setExists(true);

        // mock
        when(rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_MESSAGE_REPLYMESSAGE,
                RabbitMQConstant.REQUEST_MESSAGE_EXIST,
                parentMessageIdString
        )).thenReturn(objectMapper.writeValueAsBytes(response));

        // when
        messageExistRequester.verifyMessageIdExistsOrThrow(parentMessageId);

        // then
        // No exception is thrown
    }

    @Test
    @DisplayName("given a message id does not exist, when verifyMessageIdExistsOrThrow is called, then ResourceNotFoundException is thrown")
    void givenMessageIdDoesNotExist_whenVerifyMessageIdExistsOrThrow_thenException() throws IOException {
        // given
        Long parentMessageId = 123L;
        String parentMessageIdString = "123";
        MessageExistsResponseDTO response = new MessageExistsResponseDTO();
        response.setExists(false);

        // mock
        when(rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_MESSAGE_REPLYMESSAGE,
                RabbitMQConstant.REQUEST_MESSAGE_EXIST,
                parentMessageIdString
        )).thenReturn(objectMapper.writeValueAsBytes(response));

        // when and then
        assertThrows(ResourceNotFoundException.class, () -> messageExistRequester.verifyMessageIdExistsOrThrow(parentMessageId));
    }


}
