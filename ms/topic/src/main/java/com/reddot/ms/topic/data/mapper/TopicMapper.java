package com.reddot.ms.topic.data.mapper;

import com.reddot.ms.topic.data.dto.TopicDTO;
import com.reddot.ms.topic.data.entity.TopicEntity;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TopicMapper {

    private final ModelMapper modelMapper;

    public TopicEntity toEntity(TopicDTO topicDTO) {
        return modelMapper.map(topicDTO, TopicEntity.class);
    }

    public TopicDTO toDTO(TopicEntity topicEntity) {
        return TopicDTO.builder()
                .id(topicEntity.getId())
                .name(topicEntity.getName())
                .description(topicEntity.getDescription())
                .label(topicEntity.getLabel())
                .build();
    }
}