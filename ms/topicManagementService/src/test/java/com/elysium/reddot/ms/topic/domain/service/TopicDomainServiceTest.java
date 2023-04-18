package com.elysium.reddot.ms.topic.domain.service;

import com.elysium.reddot.ms.topic.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.topic.domain.exception.FieldWithSpaceException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopicDomainServiceTest {

    private static TopicDomainService topicDomainService;

    @BeforeAll
    static void setUp() {
        topicDomainService = new TopicDomainService();
    }

    @Test
    @DisplayName("given topicModel with null name= when validateTopicForCreation is called then throws FieldEmptyException")
    void givenTopicModelWithNullName_whenValidateBuildTopic_thenThrowsFieldEmptyException() {
        // given
        TopicModel topicModel = new TopicModel(1L, null, "test", "description");

        // then && throw
        assertThrows(FieldEmptyException.class, () -> topicDomainService.validateTopicForCreation(topicModel));
    }

    @Test
    @DisplayName("given topicModel with blank name when validateTopicForCreation is called then throws FieldEmptyException")
    void givenTopicModelWithBlankName_whenValidateBuildTopic_thenThrowsFieldEmptyException() {
        // given
        TopicModel topicModel = new TopicModel(1L, "", "test", "description");

        // then && throw
        assertThrows(FieldEmptyException.class, () -> topicDomainService.validateTopicForCreation(topicModel));
    }

    @Test
    @DisplayName("given topicModel with space name when validateTopicForCreation is called then throws FieldWithSpaceException")
    void givenTopicModelWithSpaceName_whenValidateBuildTopic_thenThrowsFieldWithSpaceException() {
        // given
        TopicModel topicModel = new TopicModel(1L, "name 1", "test", "description");

        // then && throw
        assertThrows(FieldWithSpaceException.class, () -> topicDomainService.validateTopicForCreation(topicModel));
    }

    @Test
    @DisplayName("given topicModel with null label when validateTopicForCreation is called then throws FieldEmptyException")
    void givenTopicModelWithNullLabel_whenValidateBuildTopic_thenThrowsFieldEmptyException() {
        // given
        TopicModel topicModel = new TopicModel(1L, "test", null, "description");

        // then && throw
        assertThrows(FieldEmptyException.class, () -> topicDomainService.validateTopicForCreation(topicModel));
    }

    @Test
    @DisplayName("given topicModelWith blank label when validateTopicForCreation is called then throws FieldEmptyException")
    void givenTopicModelWithBlankLabel_whenValidateBuildTopic_thenThrowsFieldEmptyException() {
        // given
        TopicModel topicModel = new TopicModel(1L, "test", " ", "description");

        // then && throw
        assertThrows(FieldEmptyException.class, () -> topicDomainService.validateTopicForCreation(topicModel));
    }

    @Test
    @DisplayName("given valid topicModel when validateTopicForCreation is called then no exception thrown")
    void givenValidTopicModel_whenValidateBuildTopic_thenNoExceptionThrown() {
        TopicModel topicModel = new TopicModel(1L, "test", "test", "description");

        assertDoesNotThrow(() -> topicDomainService.validateTopicForCreation(topicModel),
                "validateBuildTopic should not throw an exception for a valid TopicModel");
    }

    @Test
    @DisplayName("given existing topic and topic to update when updateExistingTopicWithUpdates is called then topic updated successfully")
    void givenExistingTopicAndTopicToUpdate_whenUpdateTopic_thenTopicUpdatedSuccessfully() {
        // given
        TopicModel existingTopic = new TopicModel(1L, "test", "test", "description");
        TopicModel topicToUpdate = new TopicModel(existingTopic.getId(), existingTopic.getName(), existingTopic.getLabel(), existingTopic.getDescription());
        topicToUpdate.setLabel("Updated Label");
        topicToUpdate.setDescription("Updated Description");

        // when
        TopicModel updatedTopic = topicDomainService.updateExistingTopicWithUpdates(existingTopic, topicToUpdate);

        // then
        assertEquals("Updated Label", updatedTopic.getLabel());
        assertEquals("Updated Description", updatedTopic.getDescription());
        assertEquals("test", updatedTopic.getName());
    }

    @Test
    @DisplayName("given topicModel to update with blank label when validateTopicForCreation is called then throws FieldEmptyException")
    void givenTopicModelToUpdateWithBlankLabel_whenValidateUpdateTopic_thenThrowsFieldEmptyException() {
        // given
        TopicModel existingTopic = new TopicModel(1L, "test", "test", "description");
        TopicModel topicToUpdate = new TopicModel(existingTopic.getId(), existingTopic.getName(), existingTopic.getLabel(), existingTopic.getDescription());
        topicToUpdate.setLabel("");
        topicToUpdate.setDescription("Updated Description");

        // then && throw
        assertThrows(FieldEmptyException.class, () -> topicDomainService.validateTopicForUpdate(topicToUpdate));
    }

    @Test
    @DisplayName("test hash code")
    void testHashCode() {
        TopicModel topic1 = new TopicModel(1L, "name1", "label1", "description1");
        TopicModel topic2 = new TopicModel(1L, "name2", "label2", "description2");
        TopicModel topic3 = new TopicModel(2L, "name1", "label1", "description1");
        TopicModel topic4 = new TopicModel(null, "name1", "label1", "description1");

        assertEquals(topic1.hashCode(), topic2.hashCode());
        assertNotEquals(topic1.hashCode(), topic3.hashCode());
        assertEquals(0, topic4.hashCode());
    }

    @Test
    @DisplayName("test topic equal")
    void testAreTopicsEqual() {
        TopicModel topic1 = new TopicModel(1L, "name1", "label1", "description1");
        TopicModel topic2 = new TopicModel(1L, "name2", "label2", "description2");
        TopicModel topic3 = new TopicModel(2L, "name3", "label3", "description3");
        Object otherObject = new Object();

        assertEquals(topic1, topic1);
        assertEquals(topic1, topic2);
        assertNotEquals(topic1, topic3);
        assertNotEquals(topic1, otherObject);
        assertNotEquals(null, topic1);
    }

    @Test
    @DisplayName("test to string")
    void testToString() {
        TopicModel topic = new TopicModel(1L, "name1", "label1", "description1");

        String expectedToString = "Topic{" +
                "id=" + 1L +
                ", name='name1'" +
                ", label='label1'" +
                ", description='description1'" +
                '}';

        assertEquals(expectedToString, topic.toString());
    }

}
