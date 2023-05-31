package com.elysium.reddot.ms.message.application.service;

import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.outbound.persistence.MessageRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageRabbitMQServiceTest {

    @Mock
    private MessageRepositoryAdapter messageRepositoryAdapter;

    @InjectMocks
    private MessageRabbitMQService messageRabbitMQService;


    @Test
    @DisplayName("given message with specific id exists, when checkMessageIdExists is called, then return true")
    void givenMessageExists_whenCheckMessageIdExists_thenTrue() {
        // given
        Long messageId = 1L;

        // mock
        when(messageRepositoryAdapter.findMessageById(messageId)).thenReturn(Optional.of(new MessageModel("content", 1L, "usrId")));

        // when
        boolean exists = messageRabbitMQService.checkMessageIdExists(messageId);

        // then
        assertTrue(exists);
    }

    @Test
    @DisplayName("given message with specific id does not exist, when checkMessageIdExists is called, then return false")
    void givenMessageDoesNotExist_whenCheckMessageIdExists_thenFalse() {
        // given
        Long messageId = 1L;

        // mock
        when(messageRepositoryAdapter.findMessageById(messageId)).thenReturn(Optional.empty());

        // When
        boolean exists = messageRabbitMQService.checkMessageIdExists(messageId);

        // Then
        assertFalse(exists);
    }

    @Test
    @DisplayName("given message with specific id exists and range dates, when countMessageByUserIdBetweenTwoDates is called, then return true")
    void givenMessageWithSpecificIdExistsAndRangeDates_whenCountMessageByUserIdBetweenTwoDates_thenReturnCount() {
        // given
        String userId = "u123456";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime onStart = LocalDateTime.parse("2023-05-20 09:30:00", formatter);
        LocalDateTime onEnd = LocalDateTime.parse("2023-05-25 09:30:00", formatter);
        List<MessageModel> listMessages = new ArrayList<>();

        // mock
        when(messageRepositoryAdapter.listMessagesByUserAndRangeDate("u123456", onStart, onEnd)).thenReturn(listMessages);

        // when
        int count = messageRabbitMQService.countMessageByUserIdBetweenTwoDates(userId, onStart, onEnd);

        // then
        assertEquals(0, count);
    }


}