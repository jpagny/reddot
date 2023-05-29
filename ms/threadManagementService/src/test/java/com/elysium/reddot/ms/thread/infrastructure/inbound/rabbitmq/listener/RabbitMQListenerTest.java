package com.elysium.reddot.ms.thread.infrastructure.inbound.rabbitmq.listener;

import com.elysium.reddot.ms.thread.application.service.ThreadRabbitMQService;
import com.elysium.reddot.ms.thread.infrastructure.inbound.rabbitmq.listener.ThreadRabbitMQListener;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitMQListenerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ThreadRabbitMQService threadRabbitMQService;

    private ThreadRabbitMQListener threadRabbitMQListener;

    @BeforeEach
    void setUp() {
        threadRabbitMQListener = new ThreadRabbitMQListener(rabbitTemplate, threadRabbitMQService);
    }

    @Test
    @DisplayName("given a valid message, when checkThreadExists is called, then send a reply message")
    void givenValidMessage_whenCheckThreadExists_thenSendReplyMessage() throws JsonProcessingException {
        /*
        // given
        String boardIdString = "123";
        Long boardId = 123L;
        boolean exists = true;

        // mock
        MessageConverter messageConverter = mock(MessageConverter.class);
        Message message = mock(Message.class);
        MessageProperties messageProperties = mock(MessageProperties.class);

        when(rabbitTemplate.getMessageConverter()).thenReturn(messageConverter);
        when(messageConverter.fromMessage(any())).thenReturn(boardIdString);
        when(threadRabbitMQService.checkThreadIdExists(boardId)).thenReturn(exists);
        when(message.getMessageProperties()).thenReturn(messageProperties);
        when(messageProperties.getReplyTo()).thenReturn("replyTo");
        when(messageProperties.getCorrelationId()).thenReturn("correlationId");

        // when
        threadRabbitMQListener.checkThreadExists(message);

        // then
        verify(rabbitTemplate, times(1)).send(eq("replyTo"), any(Message.class));

         */
    }
}
