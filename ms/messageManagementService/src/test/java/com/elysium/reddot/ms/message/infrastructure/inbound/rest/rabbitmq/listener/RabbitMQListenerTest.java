package com.elysium.reddot.ms.message.infrastructure.inbound.rest.rabbitmq.listener;

import com.elysium.reddot.ms.message.application.service.MessageRabbitMQService;
import com.elysium.reddot.ms.message.infrastructure.inbound.rabbitmq.listener.MessageRabbitMQListener;
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
    private MessageRabbitMQService messageRabbitMQService;

    private MessageRabbitMQListener rabbitMQListener;

    @BeforeEach
    void setUp() {
        rabbitMQListener = new MessageRabbitMQListener(rabbitTemplate, messageRabbitMQService);
    }

    @Test
    @DisplayName("given a valid message, when checkMessageExists is called, then send a reply message")
    void givenValidMessage_whenCheckBoardExists_thenSendReplyMessage() throws JsonProcessingException {
        // given
        Long messageId = 123L;
        String messageIdString = "123";
        boolean exists = true;

        // mock
        MessageConverter messageConverter = mock(MessageConverter.class);
        Message message = mock(Message.class);
        MessageProperties messageProperties = mock(MessageProperties.class);

        when(rabbitTemplate.getMessageConverter()).thenReturn(messageConverter);
        when(messageConverter.fromMessage(any())).thenReturn(messageIdString);
        when(messageRabbitMQService.checkMessageIdExists(messageId)).thenReturn(exists);
        when(message.getMessageProperties()).thenReturn(messageProperties);
        when(messageProperties.getReplyTo()).thenReturn("replyTo");
        when(messageProperties.getCorrelationId()).thenReturn("correlationId");

        // when
        rabbitMQListener.checkMessageExists(message);

        // then
        verify(rabbitTemplate, times(1)).send(eq("replyTo"), any(Message.class));
    }

    @Test
    @DisplayName("given a valid message, when countMessagesByUserBetweenTwoDates is called, then send a reply message")
    void givenValidMessage_whenCountMessagesByUserBetweenTwoDates_thenSendReplyMessage() throws JsonProcessingException {
        // given
        String jsonRequest = "{"
                + "\"userId\": \"u123456\","
                + "\"onStart\": \"2023-05-25 09:30:00\","
                + "\"onEnd\": \"2023-05-26 09:30:00\""
                + "}";

        int countMessagesInRangeDates = 100;

        // mock
        MessageConverter messageConverter = mock(MessageConverter.class);
        Message message = mock(Message.class);
        MessageProperties messageProperties = mock(MessageProperties.class);

        when(rabbitTemplate.getMessageConverter()).thenReturn(messageConverter);
        when(messageConverter.fromMessage(any())).thenReturn(jsonRequest);
        when(messageRabbitMQService.countMessageByUserIdBetweenTwoDates(any(), any(), any())).thenReturn(countMessagesInRangeDates);
        when(message.getMessageProperties()).thenReturn(messageProperties);
        when(messageProperties.getReplyTo()).thenReturn("replyTo");
        when(messageProperties.getCorrelationId()).thenReturn("correlationId");

        // when
        rabbitMQListener.countMessagesByUserBetweenTwoDates(message);

        // then
        verify(rabbitTemplate, times(1)).send(eq("replyTo"), any(Message.class));
    }
}
