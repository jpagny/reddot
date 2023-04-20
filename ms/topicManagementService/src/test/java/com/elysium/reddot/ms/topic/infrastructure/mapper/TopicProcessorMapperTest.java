package com.elysium.reddot.ms.topic.infrastructure.mapper;

import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TopicProcessorMapperTest {

    @Test
    @DisplayName("given topicModel when toDTO is called then returns topicDTO")
    void givenTopicModel_whenToDTO_thenTopicDTO() {
        // Given
        TopicModel topicModel = new TopicModel(1L, "topic1", "label1", "description1");

        // When
        TopicDTO topicDTO = TopicProcessorMapper.toDTO(topicModel);

        // Then
        assertEquals(topicModel.getId(), topicDTO.getId());
        assertEquals(topicModel.getName(), topicDTO.getName());
        assertEquals(topicModel.getLabel(), topicDTO.getLabel());
        assertEquals(topicModel.getDescription(), topicDTO.getDescription());
    }

    @Test
    @DisplayName("given list of topicModels when toDTO is called then returns list of topicDTOs")
    void givenListTopicModels_whenToDTO_thenListTopicDTOs() {
        // Given
        TopicModel topicModel1 = new TopicModel(1L, "topic1", "label1", "description1");
        TopicModel topicModel2 = new TopicModel(2L, "topic2", "label2", "description2");
        List<TopicModel> topicModels = Arrays.asList(topicModel1, topicModel2);

        // When
        List<TopicDTO> topicDTOs = TopicProcessorMapper.toDTOList(topicModels);

        // Then
        assertEquals(topicModels.size(), topicDTOs.size());
        for (int i = 0; i < topicModels.size(); i++) {
            TopicModel topicModel = topicModels.get(i);
            TopicDTO topicDTO = topicDTOs.get(i);
            assertEquals(topicModel.getId(), topicDTO.getId());
            assertEquals(topicModel.getName(), topicDTO.getName());
            assertEquals(topicModel.getLabel(), topicDTO.getLabel());
            assertEquals(topicModel.getDescription(), topicDTO.getDescription());
        }
    }

    @Test
    @DisplayName("given topicDTO when toModel is called then returns topicModel")
    void givenTopicDTO_whenToModel_thenTopicModel() {
        // Given
        TopicDTO topicDTO = new TopicDTO(1L, "topic1", "label1", "description1");

        // When
        TopicModel topicModel = TopicProcessorMapper.toModel(topicDTO);

        // Then
        assertEquals(topicDTO.getId(), topicModel.getId());
        assertEquals(topicDTO.getName(), topicModel.getName());
        assertEquals(topicDTO.getLabel(), topicModel.getLabel());
        assertEquals(topicDTO.getDescription(), topicModel.getDescription());
    }
}