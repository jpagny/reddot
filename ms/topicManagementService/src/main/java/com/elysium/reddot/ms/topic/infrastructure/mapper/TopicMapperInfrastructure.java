package com.elysium.reddot.ms.topic.infrastructure.mapper;

import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.entity.TopicJpaEntity;

public class TopicMapperInfrastructure {

    private TopicMapperInfrastructure() {
    }

    public static TopicJpaEntity toEntity(TopicDTO topicDTO) {
        TopicJpaEntity topicJpaEntity = new TopicJpaEntity();
        topicJpaEntity.setId(topicDTO.getId());
        topicJpaEntity.setName(topicDTO.getName());
        topicJpaEntity.setLabel(topicDTO.getLabel());
        topicJpaEntity.setDescription(topicDTO.getDescription());
        return topicJpaEntity;
    }

    public static TopicDTO toDTO(TopicJpaEntity topicJpaEntity) {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(topicJpaEntity.getId());
        topicDTO.setName(topicJpaEntity.getName());
        topicDTO.setLabel(topicJpaEntity.getLabel());
        topicDTO.setDescription(topicJpaEntity.getDescription());
        return topicDTO;
    }

}
