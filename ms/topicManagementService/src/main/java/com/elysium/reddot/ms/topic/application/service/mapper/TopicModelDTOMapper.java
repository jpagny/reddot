package com.elysium.reddot.ms.topic.application.service.mapper;

import com.elysium.reddot.ms.topic.application.service.dto.TopicDTO;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;

public class TopicModelDTOMapper {

    public static TopicDTO toDTO(TopicModel topicModel) {

        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(topicModel.getId());
        topicDTO.setName(topicModel.getName());
        topicDTO.setLabel(topicModel.getLabel());
        topicDTO.setDescription(topicModel.getDescription());

        return topicDTO;
    }

    public static TopicModel toDomain(TopicDTO topicDTO) {

        TopicModel topicModel = new TopicModel();
        topicModel.setId(topicDTO.getId());
        topicModel.setName(topicDTO.getName());
        topicModel.setLabel(topicDTO.getLabel());
        topicModel.setDescription(topicDTO.getDescription());

        return topicModel;
    }

}
