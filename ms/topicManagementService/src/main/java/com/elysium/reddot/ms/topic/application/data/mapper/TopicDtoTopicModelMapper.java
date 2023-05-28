package com.elysium.reddot.ms.topic.application.data.mapper;

import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This utility class provides methods for converting between TopicModel and TopicDTO objects.
 * It prevents any instances of itself from being created.
 */
public class TopicDtoTopicModelMapper {

    private TopicDtoTopicModelMapper() {
    }

    /**
     * Converts a TopicModel object to a TopicDTO object.
     *
     * @param topicModel The TopicModel object to be converted.
     * @return The converted TopicDTO object.
     */
    public static TopicDTO toDTO(TopicModel topicModel) {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(topicModel.getId());
        topicDTO.setLabel(topicModel.getLabel());
        topicDTO.setName(topicModel.getName());
        topicDTO.setDescription(topicModel.getDescription());
        return topicDTO;
    }

    /**
     * Converts a TopicDTO object to a TopicModel object.
     *
     * @param topicDTO The TopicDTO object to be converted.
     * @return The converted TopicModel object.
     */
    public static TopicModel toModel(TopicDTO topicDTO) {
        return new TopicModel(
                topicDTO.getId(),
                topicDTO.getName(),
                topicDTO.getLabel(),
                topicDTO.getDescription()
        );
    }

    /**
     * Converts a list of TopicModel objects to a list of TopicDTO objects.
     *
     * @param listTopicsModel The list of TopicModel objects to be converted.
     * @return The converted list of TopicDTO objects.
     */
    public static List<TopicDTO> toDTOList(List<TopicModel> listTopicsModel) {
        return listTopicsModel.stream()
                .map(TopicDtoTopicModelMapper::toDTO)
                .collect(Collectors.toList());
    }


}
