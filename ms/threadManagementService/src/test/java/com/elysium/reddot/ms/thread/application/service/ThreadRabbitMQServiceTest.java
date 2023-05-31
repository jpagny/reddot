package com.elysium.reddot.ms.thread.application.service;

import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.outbound.persistence.ThreadRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThreadRabbitMQServiceTest {

    @Mock
    private ThreadRepositoryAdapter threadRepositoryAdapter;

    @InjectMocks
    private ThreadRabbitMQService threadRabbitMQService;


    @Test
    @DisplayName("given thread with specific id exists, when checkThreadIdExists is called, then return true")
    void givenThreadExists_whenCheckThreadIdExists_thenTrue() {
        // given
        Long threadId = 1L;

        // mock
        when(threadRepositoryAdapter.findThreadById(threadId)).thenReturn(Optional.of(new ThreadModel(threadId, "name", "label", "description", 1L, "userId")));

        // when
        boolean exists = threadRabbitMQService.checkThreadIdExists(threadId);

        // then
        assertTrue(exists);
    }

    @Test
    @DisplayName("given thread with specific id does not exist, when checkThreadIdExists is called, then return false")
    void givenThreadDoesNotExist_whenCheckThreadIdExists_thenFalse() {
        // given
        Long threadId = 1L;

        // mock
        when(threadRepositoryAdapter.findThreadById(threadId)).thenReturn(Optional.empty());

        // When
        boolean exists = threadRabbitMQService.checkThreadIdExists(threadId);

        // Then
        assertFalse(exists);
    }

}
