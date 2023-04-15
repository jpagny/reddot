package com.elysium.reddot.ms.topic.domain.service;

import com.elysium.reddot.ms.topic.domain.exception.LabelEmptyException;
import com.elysium.reddot.ms.topic.domain.exception.NameEmptyException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopicDomainServiceTest {

    private static TopicDomainService topicDomainService;

    @BeforeAll
    static void setUp() {
        topicDomainService = new TopicDomainService();
    }

    @Test
    void givenTopicModelWithNullName_whenValidateBuildTopic_thenThrowsNameEmptyException() {
        // given
        TopicModel topicModel = new TopicModel(1L, null, "test", "description");

        // then && throw
        assertThrows(NameEmptyException.class, () -> topicDomainService.validateBuildTopic(topicModel));
    }

    @Test
    void givenTopicModelWithBlankName_whenValidateBuildTopic_thenThrowsNameEmptyException() {
        // given
        TopicModel topicModel = new TopicModel(1L, "", "test", "description");

        // then && throw
        assertThrows(NameEmptyException.class, () -> topicDomainService.validateBuildTopic(topicModel));
    }

    @Test
    void givenTopicModelWithNullLabel_whenValidateBuildTopic_thenThrowsLabelEmptyException() {
        // given
        TopicModel topicModel = new TopicModel(1L, "test", null, "description");

        // then && throw
        assertThrows(LabelEmptyException.class, () -> topicDomainService.validateBuildTopic(topicModel));
    }

    @Test
    void givenTopicModelWithBlankLabel_whenValidateBuildTopic_thenThrowsLabelEmptyException() {
        // given
        TopicModel topicModel = new TopicModel(1L, "test", " ", "description");

        // then && throw
        assertThrows(LabelEmptyException.class, () -> topicDomainService.validateBuildTopic(topicModel));
    }

    @Test
    void givenValidTopicModel_whenValidateBuildTopic_thenNoExceptionThrown() {
        TopicModel topicModel = new TopicModel(1L, "test", "test", "description");

        assertDoesNotThrow(() -> topicDomainService.validateBuildTopic(topicModel),
                "validateBuildTopic should not throw an exception for a valid TopicModel");
    }

    @Test
    void givenExistingTopicAndTopicToUpdate_whenUpdateTopic_thenTopicUpdatedSuccessfully() {
        // given
        TopicModel existingTopic = new TopicModel(1L, "test", "test", "description");
        TopicModel topicToUpdate = new TopicModel(existingTopic.getId(), existingTopic.getName(), existingTopic.getLabel(), existingTopic.getDescription());
        topicToUpdate.setLabel("Updated Label");
        topicToUpdate.setDescription("Updated Description");

        // when
        TopicModel updatedTopic = topicDomainService.updateTopic(existingTopic, topicToUpdate);

        // then
        assertEquals("Updated Label", updatedTopic.getLabel());
        assertEquals("Updated Description", updatedTopic.getDescription());
        assertEquals("test", updatedTopic.getName());
    }

    @Test
    void givenTopicModelToUpdateWithBlankLabel_whenValidateUpdateTopic_thenThrowsLabelEmptyException() {
        // given
        TopicModel existingTopic = new TopicModel(1L, "test", "test", "description");
        TopicModel topicToUpdate = new TopicModel(existingTopic.getId(), existingTopic.getName(), existingTopic.getLabel(), existingTopic.getDescription());
        topicToUpdate.setLabel("");
        topicToUpdate.setDescription("Updated Description");

        // then && throw
        assertThrows(LabelEmptyException.class, () -> topicDomainService.validateBuildTopic(topicToUpdate));
    }


}
