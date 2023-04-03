package com.reddot.ms.topic.mapper;

import com.reddot.ms.topic.dto.TopicDTO;
import com.reddot.ms.topic.entity.TopicEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TopicMapperTest {

    private TopicMapper topicMapper;

    @BeforeEach
    public void setUp() {
        topicMapper = new TopicMapper(new ModelMapper());
    }

    @Test
    @DisplayName("Given topicDTO, when mapping ToEntity, then the maps correctly")
    public void givenTopicDTO_whenMappingToEntity_thenMapsCorrectly() {
        TopicDTO topicDTO = TopicDTO.builder()
                .id(1L)
                .name("elysium")
                .label("Elysium")
                .description("About elysium")
                .build();

        TopicEntity expectedTopicEntity = new TopicEntity(
                1L,
                "elysium",
                "Elysium",
                "About elysium"
        );

        TopicEntity actualTopicEntity = topicMapper.toEntity(topicDTO);

        assertThat(actualTopicEntity.getId()).isEqualTo(expectedTopicEntity.getId());
    }

    @Test
    @DisplayName("Given topicEntity, when mapping ToDTO, then the maps correctly")
    public void givenTopicEntity_whenMappingToDTO_thenMapsCorrectly() {
        TopicEntity topicEntity = new TopicEntity(
                1L,
                "elysium",
                "Elysium",
                "About elysium"
        );

        TopicDTO expectedTopicDTO = TopicDTO.builder()
                .id(1L)
                .name("elysium")
                .label("Elysium")
                .description("About elysium")
                .build();

        TopicDTO actualTopicDTO = topicMapper.toDTO(topicEntity);

        assertThat(actualTopicDTO).usingRecursiveComparison().isEqualTo(expectedTopicDTO);
    }


}
