package com.elysium.reddot.ms.topic.application.service;

import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.TopicRepositoryAdapter;
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
class TopicRabbitMQServiceTest {

    @Mock
    private TopicRepositoryAdapter topicJpaRepository;

    @InjectMocks
    private TopicRabbitMQService topicRabbitMQService;


    @Test
    @DisplayName("given topic with specific id exists, when checkTopicIdExists is called, then return true")
    void givenTopicExists_whenCheckTopicIdExists_thenTrue() {
        // given
        Long topicId = 1L;

        // mock
        when(topicJpaRepository.findTopicById(topicId)).thenReturn(Optional.of(new TopicModel(topicId, "name", "label", "description")));

        // when
        boolean exists = topicRabbitMQService.checkTopicIdExists(topicId);

        // then
        assertTrue(exists);
    }

    @Test
    @DisplayName("given topic with specific id does not exist, when checkTopicIdExists is called, then return false")
    void givenTopicDoesNotExist_whenCheckTopicIdExists_thenFalse() {
        // given
        Long topicId = 1L;

        // mock
        when(topicJpaRepository.findTopicById(topicId)).thenReturn(Optional.empty());

        // When
        boolean exists = topicRabbitMQService.checkTopicIdExists(topicId);

        // Then
        assertFalse(exists);
    }

}
