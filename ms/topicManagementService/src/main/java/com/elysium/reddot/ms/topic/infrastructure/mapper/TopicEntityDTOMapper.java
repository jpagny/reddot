package com.elysium.reddot.ms.topic.infrastructure.mapper;

import com.elysium.reddot.ms.topic.application.service.dto.TopicDTO;
import com.elysium.reddot.ms.topic.infrastructure.adapter.outbound.persistence.TopicJpaEntity;

public class TopicEntityDTOMapper {

    public static TopicJpaEntity toEntity(TopicDTO topicDTO) {
        TopicJpaEntity topicJpaEntity = new TopicJpaEntity();
        topicJpaEntity.setId(topicDTO.getId());
        topicJpaEntity.setName(topicDTO.getName());
        return topicJpaEntity;
    }

    public static TopicDTO toDTO(TopicJpaEntity topicJpaEntity) {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(topicJpaEntity.getId());
        topicDTO.setName(topicJpaEntity.getName());
        return topicDTO;
    }

}
