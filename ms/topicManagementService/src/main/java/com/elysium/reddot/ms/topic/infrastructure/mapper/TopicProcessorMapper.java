package com.elysium.reddot.ms.topic.infrastructure.mapper;

import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;

import java.util.List;
import java.util.stream.Collectors;

public class TopicProcessorMapper {

    private TopicProcessorMapper() {
    }

    public static TopicDTO toDTO(TopicModel topicModel) {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(topicModel.getId());
        topicDTO.setLabel(topicModel.getLabel());
        topicDTO.setName(topicModel.getName());
        topicDTO.setDescription(topicModel.getDescription());
        return topicDTO;
    }

    public static List<TopicDTO> toDTOList(List<TopicModel> topicModels) {
        return topicModels.stream()
                .map(TopicProcessorMapper::toDTO)
                .collect(Collectors.toList());
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
