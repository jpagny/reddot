package com.elysium.reddot.ms.board.infrastructure.inbound.rabbitmq.listener;

import com.elysium.reddot.ms.board.application.service.BoardRabbitMQService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitMQListenerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private BoardRabbitMQService boardRabbitMQService;

    private BoardRabbitMQListener rabbitMQListener;

    @BeforeEach
    void setUp() {
        rabbitMQListener = new BoardRabbitMQListener(rabbitTemplate, boardRabbitMQService, new ObjectMapper());
    }

    @Test
    @DisplayName("given a valid message, when checkBoardExists is called, then send a reply message")
    void givenValidMessage_whenCheckBoardExists_thenSendReplyMessage() throws IOException {
        // given
        String boardIdString = "123";
        byte[] boardIdBytes = boardIdString.getBytes(StandardCharsets.UTF_8);
        Long boardId = 123L;
        boolean exists = true;

        // mock
        MessageConverter messageConverter = mock(MessageConverter.class);
        Message message = mock(Message.class);
        MessageProperties messageProperties = mock(MessageProperties.class);

        when(rabbitTemplate.getMessageConverter()).thenReturn(messageConverter);
        when(messageConverter.fromMessage(any())).thenReturn(boardIdBytes);
        when(boardRabbitMQService.checkBoardIdExists(boardId)).thenReturn(exists);
        when(message.getMessageProperties()).thenReturn(messageProperties);
        when(messageProperties.getReplyTo()).thenReturn("replyTo");
        when(messageProperties.getCorrelationId()).thenReturn("correlationId");

        // when
        rabbitMQListener.checkBoardExists(message);

        // then
        verify(rabbitTemplate, times(1)).send(eq("replyTo"), any(Message.class));
    }
}
