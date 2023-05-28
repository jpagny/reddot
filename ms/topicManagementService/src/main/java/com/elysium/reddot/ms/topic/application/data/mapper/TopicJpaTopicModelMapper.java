package com.elysium.reddot.ms.topic.application.data.mapper;

import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.entity.TopicJpaEntity;

/**
 * Mapper class for converting between TopicModel and TopicJpaEntity.
 */
public class TopicJpaTopicModelMapper {

    private TopicJpaTopicModelMapper() {
    }

    /**
     * Converts a TopicModel object to a TopicJpaEntity object.
     *
     * @param topicModel The TopicModel to be converted.
     * @return The converted TopicJpaEntity.
     */
    public static TopicJpaEntity toEntity(TopicModel topicModel) {
        TopicJpaEntity topicJpaEntity = new TopicJpaEntity();
        topicJpaEntity.setId(topicModel.getId());
        topicJpaEntity.setName(topicModel.getName());
        topicJpaEntity.setLabel(topicModel.getLabel());
        topicJpaEntity.setDescription(topicModel.getDescription());
        return topicJpaEntity;
    }

    /**
     * Converts a TopicJpaEntity object to a TopicModel object.
     *
     * @param topicJpaEntity The TopicJpaEntity to be converted.
     * @return The converted TopicModel.
     */
    public static TopicModel toModel(TopicJpaEntity topicJpaEntity) {
        return new TopicModel(
                topicJpaEntity.getId(),
                topicJpaEntity.getName(),
                topicJpaEntity.getLabel(),
                topicJpaEntity.getDescription()
        );
    }

}
