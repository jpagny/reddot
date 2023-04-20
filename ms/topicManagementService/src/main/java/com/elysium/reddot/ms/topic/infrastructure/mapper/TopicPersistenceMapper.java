package com.elysium.reddot.ms.topic.infrastructure.mapper;

import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.entity.TopicJpaEntity;

public class TopicPersistenceMapper {

    private TopicPersistenceMapper() {
    }

    public static TopicJpaEntity toEntity(TopicModel topicModel) {
        TopicJpaEntity topicJpaEntity = new TopicJpaEntity();
        topicJpaEntity.setId(topicModel.getId());
        topicJpaEntity.setName(topicModel.getName());
        topicJpaEntity.setLabel(topicModel.getLabel());
        topicJpaEntity.setDescription(topicModel.getDescription());
        return topicJpaEntity;
    }

    public static TopicModel toModel(TopicJpaEntity topicJpaEntity) {
        return new TopicModel(
                topicJpaEntity.getId(),
                topicJpaEntity.getName(),
                topicJpaEntity.getLabel(),
                topicJpaEntity.getDescription()
        );
    }

}
