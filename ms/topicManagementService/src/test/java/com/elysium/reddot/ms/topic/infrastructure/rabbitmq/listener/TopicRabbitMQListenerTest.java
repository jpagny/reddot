package com.elysium.reddot.ms.topic.infrastructure.rabbitmq.listener;

import com.elysium.reddot.ms.topic.application.service.TopicRabbitMQService;
import com.elysium.reddot.ms.topic.infrastructure.inbound.rabbitmq.TopicRabbitMQListener;
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
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicRabbitMQListenerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private TopicRabbitMQService topicRabbitMQService;

    private TopicRabbitMQListener topicRabbitMQListener;

    @BeforeEach
    void setUp() {
        topicRabbitMQListener = new TopicRabbitMQListener(rabbitTemplate, topicRabbitMQService);
    }

    @Test
    @DisplayName("given a valid message, when checkTopicExists is called, then send a reply message")
    void givenValidMessage_whenCheckBoardExists_thenSendReplyMessage() throws IOException {

        // given
        String topicIdString = "123";
        byte[] topicIdBytes = topicIdString.getBytes(StandardCharsets.UTF_8);
        Long topicId = 123L;
        boolean exists = true;

        // mock
        MessageConverter messageConverter = mock(MessageConverter.class);
        Message message = mock(Message.class);
        MessageProperties messageProperties = mock(MessageProperties.class);

        when(rabbitTemplate.getMessageConverter()).thenReturn(messageConverter);
        when(messageConverter.fromMessage(any())).thenReturn(topicIdBytes);
        when(topicRabbitMQService.checkTopicIdExists(topicId)).thenReturn(exists);
        when(message.getMessageProperties()).thenReturn(messageProperties);
        when(messageProperties.getReplyTo()).thenReturn("replyTo");
        when(messageProperties.getCorrelationId()).thenReturn("correlationId");

        // when
        topicRabbitMQListener.checkTopicExists(message);

        // then
        verify(rabbitTemplate, times(1)).send(eq("replyTo"), any(Message.class));
    }

}
