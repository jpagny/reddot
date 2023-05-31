package com.elysium.reddot.ms.topic.application.data.mapper;

import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TopicApplicationMapperTest {

    @Test
    @DisplayName("given topicModel when toDTO is called then returns TopicDTO")
    void givenTopicModel_whenToDTO_thenTopicDTO() {
        // given
        TopicModel topicModel = new TopicModel(1L, "Test Name", "Test Label", "Test Description");

        // when
        TopicDTO topicDTO = TopicDtoTopicModelMapper.toDTO(topicModel);

        // then
        assertEquals(topicModel.getId(), topicDTO.getId(), "The topic ID should match");
        assertEquals(topicModel.getName(), topicDTO.getName(), "The topic name should match");
        assertEquals(topicModel.getLabel(), topicDTO.getLabel(), "The topic label should match");
        assertEquals(topicModel.getDescription(), topicDTO.getDescription(), "The topic description should match");
    }

    @Test
    @DisplayName("given topicDTO when toModel is called then returns topicModel")
    void givenTopicDTO_whenToModel_thenTopicModel() {
        // given
        TopicDTO topicDTO = new TopicDTO(1L, "Test Name", "Test Label", "Test Description");

        // when
        TopicModel topicModel = TopicDtoTopicModelMapper.toModel(topicDTO);

        // then
        assertEquals(topicDTO.getId(), topicModel.getId(), "The topic ID should match");
        assertEquals(topicDTO.getName(), topicModel.getName(), "The topic name should match");
        assertEquals(topicDTO.getLabel(), topicModel.getLabel(), "The topic label should match");
        assertEquals(topicDTO.getDescription(), topicModel.getDescription(), "The topic description should match");
    }


}
