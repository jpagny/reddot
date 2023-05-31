package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.rabbitmq.listener;

import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageRabbitMQService;
import com.elysium.reddot.ms.replymessage.infrastructure.inbound.rabbitmq.listener.ReplyMessageRabbitMQListener;
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
    private ReplyMessageRabbitMQService replyMessageRabbitMQService;

    private ReplyMessageRabbitMQListener replyMessageRabbitMQListener;

    @BeforeEach
    void setUp() {
        replyMessageRabbitMQListener = new ReplyMessageRabbitMQListener(rabbitTemplate, replyMessageRabbitMQService);
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
        when(replyMessageRabbitMQService.countRepliesMessageByUserIdBetweenTwoDates(any(), any(), any())).thenReturn(countMessagesInRangeDates);
        when(message.getMessageProperties()).thenReturn(messageProperties);
        when(messageProperties.getReplyTo()).thenReturn("replyTo");
        when(messageProperties.getCorrelationId()).thenReturn("correlationId");

        // when
        replyMessageRabbitMQListener.countMessagesByUserBetweenTwoDates(message);

        // then
        verify(rabbitTemplate, times(1)).send(eq("replyTo"), any(Message.class));
    }
}
