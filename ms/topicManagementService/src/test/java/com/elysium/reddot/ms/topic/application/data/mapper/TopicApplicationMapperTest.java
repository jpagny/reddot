package com.elysium.reddot.ms.topic.application.data.mapper;

import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TopicApplicationMapperTest {

    @Test
    void givenTopicModel_whenToDTO_thenTopicDTO() {
        // given
        TopicModel topicModel = new TopicModel(1L, "Test Name", "Test Label", "Test Description");

        // when
        TopicDTO topicDTO = TopicApplicationMapper.toDTO(topicModel);

        // then
        assertEquals(topicModel.getId(), topicDTO.getId(), "The topic ID should match");
        assertEquals(topicModel.getName(), topicDTO.getName(), "The topic name should match");
        assertEquals(topicModel.getLabel(), topicDTO.getLabel(), "The topic label should match");
        assertEquals(topicModel.getDescription(), topicDTO.getDescription(), "The topic description should match");
    }

    @Test
    void givenTopicDTO_whenToModel_thenTopicModel() {
        // given
        TopicDTO topicDTO = new TopicDTO(1L, "Test Name", "Test Label", "Test Description");

        // when
        TopicModel topicModel = TopicApplicationMapper.toModel(topicDTO);

        // then
        assertEquals(topicDTO.getId(), topicModel.getId(), "The topic ID should match");
        assertEquals(topicDTO.getName(), topicModel.getName(), "The topic name should match");
        assertEquals(topicDTO.getLabel(), topicModel.getLabel(), "The topic label should match");
        assertEquals(topicDTO.getDescription(), topicModel.getDescription(), "The topic description should match");
    }


}
