package com.elysium.reddot.ms.replymessage.application.service;

import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.ReplyMessageRepositoryAdapter;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageRabbitMQServiceTest {

    @Mock
    private ReplyMessageRepositoryAdapter replyMessageRepositoryAdapter;

    @InjectMocks
    private ReplyMessageRabbitMQService replyMessageRabbitMQService;

    @Test
    @DisplayName("given message with specific id exists and range dates, when countMessageByUserIdBetweenTwoDates is called, then return true")
    void givenMessageWithSpecificIdExistsAndRangeDates_whenCountMessageByUserIdBetweenTwoDates_thenReturnCount() {
        // given
        String userId = "u123456";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime onStart = LocalDateTime.parse("2023-05-20 09:30:00", formatter);
        LocalDateTime onEnd = LocalDateTime.parse("2023-05-25 09:30:00", formatter);
        List<ReplyMessageModel> listMessages = new ArrayList<>();

        // mock
        when(replyMessageRepositoryAdapter.listMessagesByUserAndRangeDate("u123456", onStart, onEnd)).thenReturn(listMessages);

        // when
        int count = replyMessageRabbitMQService.countRepliesMessageByUserIdBetweenTwoDates(userId, onStart, onEnd);

        // then
        assertEquals(0, count);
    }


}