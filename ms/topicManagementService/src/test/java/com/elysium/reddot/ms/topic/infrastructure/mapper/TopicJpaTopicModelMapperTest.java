package com.elysium.reddot.ms.topic.infrastructure.mapper;

import com.elysium.reddot.ms.topic.application.data.mapper.TopicJpaTopicModelMapper;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.entity.TopicJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TopicJpaTopicModelMapperTest {

    @Test
    @DisplayName("given topicModel when toEntity is called then returns topicJpaEntity")
    void givenTopicModel_whenToEntity_thenTopicJpaEntity() {
        // given
        TopicModel topicModel = new TopicModel(1L, "Test Name", "Test Label", "Test Description");

        // when
        TopicJpaEntity topicJpaEntity = TopicJpaTopicModelMapper.toEntity(topicModel);

        // then
        assertEquals(topicModel.getId(), topicJpaEntity.getId(), "The topic ID should match");
        assertEquals(topicModel.getName(), topicJpaEntity.getName(), "The topic name should match");
        assertEquals(topicModel.getLabel(), topicJpaEntity.getLabel(), "The topic label should match");
        assertEquals(topicModel.getDescription(), topicJpaEntity.getDescription(), "The topic description should match");
    }

    @Test
    @DisplayName("given topicJpaEntity when toModel is called then returns topicModel")
    void givenTopicJpaEntity_whenToModel_thenTopicModel() {
        // given
        TopicJpaEntity topicJpaEntity = new TopicJpaEntity();
        topicJpaEntity.setId(1L);
        topicJpaEntity.setName("Test Name");
        topicJpaEntity.setLabel("Test Label");
        topicJpaEntity.setDescription("Test Description");

        // when
        TopicModel topicModel = TopicJpaTopicModelMapper.toModel(topicJpaEntity);

        // then
        assertEquals(topicJpaEntity.getId(), topicModel.getId(), "The topic ID should match");
        assertEquals(topicJpaEntity.getName(), topicModel.getName(), "The topic name should match");
        assertEquals(topicJpaEntity.getLabel(), topicModel.getLabel(), "The topic label should match");
        assertEquals(topicJpaEntity.getDescription(), topicModel.getDescription(), "The topic description should match");
    }


}
