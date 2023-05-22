package com.elysium.reddot.ms.topic.infrastructure.rabbitmq.listener;

import com.elysium.reddot.ms.topic.application.service.TopicRabbitMQService;
import com.elysium.reddot.ms.topic.infrastructure.inbound.rest.rabbitMQ.TopicRabbitMQListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopicRabbitMQListenerTest {

    @InjectMocks
    private TopicRabbitMQListener topicRabbitMQListener;

    @Mock
    private TopicRabbitMQService topicRabbitMQService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private MessageConverter messageConverter;

    private LogCaptor logCaptor;

    @BeforeEach
    void setUp() {
        logCaptor = LogCaptor.forClass(TopicRabbitMQListener.class);
    }

    @Test
    @DisplayName("Given valid message, when checkTopicExists, then success")
    void givenValidMessage_whenCheckTopicExists_thenSuccess() throws JsonProcessingException {
        // given
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReplyTo("my.reply.queue");

        Message message = new Message("Test message".getBytes(), messageProperties);

        // mock
        when(rabbitTemplate.getMessageConverter()).thenReturn(messageConverter);
        when(messageConverter.fromMessage(message)).thenReturn(1L);
        when(topicRabbitMQService.checkTopicIdExists(1L)).thenReturn(true);
        doNothing().when(rabbitTemplate).send(eq(messageProperties.getReplyTo()), any(Message.class));

        // when
        topicRabbitMQListener.checkTopicExists(message);

        // then
        verify(topicRabbitMQService).checkTopicIdExists(1L);
        assertTrue(logCaptor.getDebugLogs().contains("Check topic exists: true"));
        verify(rabbitTemplate, times(1)).send(eq(messageProperties.getReplyTo()), any(Message.class));
    }

}
