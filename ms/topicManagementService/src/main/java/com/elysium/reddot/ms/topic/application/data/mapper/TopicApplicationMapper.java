package com.elysium.reddot.ms.topic.application.data.mapper;

import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;

public class TopicApplicationMapper {

    public static TopicDTO toDTO(TopicModel topicModel) {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(topicModel.getId());
        topicDTO.setLabel(topicModel.getLabel());
        topicDTO.setName(topicModel.getName());
        topicDTO.setDescription(topicModel.getDescription());
        return topicDTO;
    }

    public static TopicModel toModel(TopicDTO topicDTO) {
        return new TopicModel(
                topicDTO.getId(),
                topicDTO.getName(),
                topicDTO.getLabel(),
                topicDTO.getDescription()
        );
    }

}
