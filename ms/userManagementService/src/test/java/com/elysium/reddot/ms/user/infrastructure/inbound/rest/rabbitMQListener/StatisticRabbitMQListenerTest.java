package com.elysium.reddot.ms.user.infrastructure.inbound.rest.rabbitMQListener;

import com.elysium.reddot.ms.user.application.service.UserRabbitMQService;
import com.elysium.reddot.ms.user.infrastructure.inbound.rabbitMQ.listener.StatisticRabbitMQListener;
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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticRabbitMQListenerTest {

    @InjectMocks
    private StatisticRabbitMQListener statisticRabbitMQListener;

    @Mock
    private UserRabbitMQService userRabbitMQService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    private LogCaptor logCaptor;


    @BeforeEach
    public void setUp() {
        logCaptor = LogCaptor.forClass(StatisticRabbitMQListener.class);
    }

    @Test
    @DisplayName("Given valid message, when getAllUserIds, then success")
    public void givenValidMessage_whenGetAllUserIds_thenSuccess() throws JsonProcessingException {
        // given
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReplyTo("my.reply.queue");

        Message message = new Message("Test message".getBytes(), messageProperties);
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add("user1");
        userIds.add("user2");

        // mock
        when(userRabbitMQService.getAllUsers()).thenReturn(userIds);
        doNothing().when(rabbitTemplate).send(eq(messageProperties.getReplyTo()), any(Message.class));

        // when
        statisticRabbitMQListener.getAllUserIds(message);

        // then
        verify(userRabbitMQService).getAllUsers();
        assertTrue(logCaptor.getDebugLogs().contains("Retrieved user IDs: [user1, user2]"));
        verify(rabbitTemplate, times(1)).send(eq(messageProperties.getReplyTo()), any(Message.class));
    }


}
