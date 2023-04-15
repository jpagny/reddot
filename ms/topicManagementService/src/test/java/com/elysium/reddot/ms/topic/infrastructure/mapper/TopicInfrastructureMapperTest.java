package com.elysium.reddot.ms.topic.infrastructure.mapper;

import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.entity.TopicJpaEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TopicInfrastructureMapperTest {

    @Test
    void givenTopicDTO_whenToEntity_thenTopicJpaEntity() {
        // given
        TopicDTO topicDTO = new TopicDTO(1L, "Test Name", "Test Label", "Test Description");

        // when
        TopicJpaEntity topicJpaEntity = TopicInfrastructureMapper.toEntity(topicDTO);

        // then
        assertEquals(topicDTO.getId(), topicJpaEntity.getId(), "The topic ID should match");
        assertEquals(topicDTO.getName(), topicJpaEntity.getName(), "The topic name should match");
        assertEquals(topicDTO.getLabel(), topicJpaEntity.getLabel(), "The topic label should match");
        assertEquals(topicDTO.getDescription(), topicJpaEntity.getDescription(), "The topic description should match");
    }

    @Test
    void givenTopicJpaEntity_whenToDTO_thenTopicDTO() {
        // given
        TopicJpaEntity topicJpaEntity = new TopicJpaEntity();
        topicJpaEntity.setId(1L);
        topicJpaEntity.setName("Test Name");
        topicJpaEntity.setLabel("Test Label");
        topicJpaEntity.setDescription("Test Description");

        // when
        TopicDTO topicDTO = TopicInfrastructureMapper.toDTO(topicJpaEntity);

        // then
        assertEquals(topicJpaEntity.getId(), topicDTO.getId(), "The topic ID should match");
        assertEquals(topicJpaEntity.getName(), topicDTO.getName(), "The topic name should match");
        assertEquals(topicJpaEntity.getLabel(), topicDTO.getLabel(), "The topic label should match");
        assertEquals(topicJpaEntity.getDescription(), topicDTO.getDescription(), "The topic description should match");
    }


}
