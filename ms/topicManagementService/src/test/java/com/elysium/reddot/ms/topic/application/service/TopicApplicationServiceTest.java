package com.elysium.reddot.ms.topic.application.service;

import com.elysium.reddot.ms.topic.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.topic.application.exception.type.ResourceBadValueException;
import com.elysium.reddot.ms.topic.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.TopicRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicApplicationServiceTest {

    private TopicApplicationServiceImpl topicService;
    @Mock
    private TopicRepositoryAdapter topicRepository;

    @BeforeEach
    void setUp() {
        topicService = new TopicApplicationServiceImpl(topicRepository);
    }

    @Test
    @DisplayName("given valid id when getTopicById is called then returns topic")
    void givenValidId_whenGetTopicById_thenReturnsTopic() {
        // given
        Long validId = 1L;
        TopicModel expectedTopic = new TopicModel(1L, "test", "Test Name", "Test Description");

        // mock
        when(topicRepository.findTopicById(validId)).thenReturn(Optional.of(expectedTopic));

        // when
        TopicModel actualTopic = topicService.getTopicById(validId);

        // then
        assertEquals(expectedTopic, actualTopic);
        verify(topicRepository, times(1)).findTopicById(validId);
    }

    @Test
    @DisplayName("given invalid id when getTopicById is called then throws ResourceNotFoundException")
    void givenInvalidId_whenGetTopicById_thenThrowsResourceNotFoundException() {
        // given
        Long invalidId = 99L;

        // mock
        when(topicRepository.findTopicById(invalidId)).thenReturn(Optional.empty());

        // then && when
        assertThrows(ResourceNotFoundException.class,
                () -> topicService.getTopicById(invalidId),
                "getTopicById should throw a ResourceNotFoundException for an invalid ID");
        verify(topicRepository, times(1)).findTopicById(invalidId);
    }

    @Test
    @DisplayName("given existing topics, when getAll, then returns List of topics")
    void givenExistingTopics_whenGetAll_thenReturnsListOfTopics() {
        // given
        TopicModel topic1Model = new TopicModel(1L, "test1", "Test Name 1", "Test Description 1");
        TopicModel topic2Model = new TopicModel(2L, "test2", "Test Name 2", "Test Description 2");

        // mock
        List<TopicModel> expectedListTopics = Arrays.asList(topic1Model, topic2Model);
        when(topicService.getAllTopics()).thenReturn(expectedListTopics);

        // when
        List<TopicModel> actualTopics = topicService.getAllTopics();

        // then
        assertEquals(expectedListTopics, actualTopics, "The returned topic list should match the expected topic list");
        verify(topicRepository, times(1)).findAllTopics();
    }

    @Test
    @DisplayName("given valid topic when createTopic is called then topic created")
    void givenValidTopic_whenCreateTopic_thenTopicCreated() {
        // given
        TopicModel topicToCreateLabel = new TopicModel(null, "test", "Test Label", "Test Description");
        TopicModel expectedTopic = new TopicModel(1L, "test", "Test Label", "Test Description");

        // mock
        when(topicRepository.findTopicByName(topicToCreateLabel.getName())).thenReturn(Optional.empty());
        when(topicRepository.createTopic(topicToCreateLabel)).thenReturn(expectedTopic);

        // when
        TopicModel actualTopicModel = topicService.createTopic(topicToCreateLabel);

        // then
        assertEquals(expectedTopic, actualTopicModel, "The created topic should match the expected topic");
        verify(topicRepository, times(1)).findTopicByName(expectedTopic.getName());
        verify(topicRepository, times(1)).createTopic(topicToCreateLabel);
    }

    @Test
    @DisplayName("given existing topic when createTopic is called then throws ResourceAlreadyExistException")
    void givenExistingTopic_whenCreateTopic_thenThrowsResourceAlreadyExistException() {
        // given
        TopicModel existingTopicModel = new TopicModel(1L, "Test Name", "Test Label", "Test Description");

        // mock
        when(topicRepository.findTopicByName(existingTopicModel.getName())).thenReturn(Optional.of(existingTopicModel));

        // when && then
        assertThrows(ResourceAlreadyExistException.class,
                () -> topicService.createTopic(existingTopicModel),
                "createTopic should throw a ResourceAlreadyExistException for an existing topic");
        verify(topicRepository, times(1)).findTopicByName(existingTopicModel.getName());
    }

    @Test
    @DisplayName("given invalid topic when createTopic is called then throws ResourceBadValueException")
    void givenInvalidTopic_whenCreateTopic_thenThrowsResourceBadValueException() {
        // given
        TopicModel invalidTopicModel = new TopicModel(1L, "", "Invalid Label", "Invalid Description");

        // mock
        when(topicRepository.findTopicByName(invalidTopicModel.getName())).thenReturn(Optional.empty());

        // when && then
        assertThrows(ResourceBadValueException.class,
                () -> topicService.createTopic(invalidTopicModel),
                "createTopic should throw a ResourceBadValueException for an invalid topic");
        verify(topicRepository, times(1)).findTopicByName(invalidTopicModel.getName());
    }

    @Test
    @DisplayName("given validTopic when updateTopic is called then topic updated")
    void givenValidTopic_whenUpdateTopic_thenTopicUpdated() {
        // given
        Long topicId = 1L;
        TopicModel existingTopicModel = new TopicModel(topicId, "Old Name", "OldLabel", "Old Description");
        TopicModel topicToUpdateModel = new TopicModel(topicId, "Old Name", "NewLabel", "NewDescription");
        TopicModel expectedTopic = new TopicModel(topicId, "Old Name", "NewLabel", "NewDescription");

        // mock
        when(topicRepository.findTopicById(topicId)).thenReturn(Optional.of(existingTopicModel));
        when(topicRepository.updateTopic(expectedTopic)).thenReturn(expectedTopic);

        // when
        TopicModel actualTopicDTO = topicService.updateTopic(1L, topicToUpdateModel);

        // then
        assertEquals(expectedTopic, actualTopicDTO, "The updated topic should match the expected topic");
        verify(topicRepository, times(1)).findTopicById(topicId);
        verify(topicRepository, times(1)).updateTopic(expectedTopic);
    }

    @Test
    @DisplayName("given non-existent topic when updateTopic is called then throws ResourceNotFoundException")
    void givenNonExistentTopic_whenUpdateTopic_thenThrowsResourceNotFoundException() {
        // given
        Long nonExistentTopicId = 99L;
        TopicModel topicToUpdateModel = new TopicModel(nonExistentTopicId, "New Name", "New Label", "New Description");

        // mock
        when(topicRepository.findTopicById(nonExistentTopicId)).thenReturn(Optional.empty());

        // then && when
        assertThrows(ResourceNotFoundException.class,
                () -> topicService.updateTopic(nonExistentTopicId, topicToUpdateModel),
                "updateTopic should throw a ResourceNotFoundException for a non-existent topic");
        verify(topicRepository, times(1)).findTopicById(nonExistentTopicId);
    }

    @Test
    @DisplayName("given invalid topic when updateTopic is called then throws ResourceBadValueException")
    void givenInvalidTopic_whenUpdateTopic_thenThrowsResourceBadValueException() {
        // given
        Long topicId = 1L;
        TopicModel existingTopicModel = new TopicModel(topicId, "Old Name", "Old Label", "Old Description");
        TopicModel invalidTopicToUpdateModel = new TopicModel(topicId, "Invalid Name", "", "Invalid Description");

        // mock
        when(topicRepository.findTopicById(topicId)).thenReturn(Optional.of(existingTopicModel));

        // when && then
        assertThrows(ResourceBadValueException.class,
                () -> topicService.updateTopic
                        (topicId, invalidTopicToUpdateModel),
                "updateTopic should throw a ResourceBadValueException for an invalid topic");
        verify(topicRepository, times(1)).findTopicById(topicId);
    }

    @Test
    @DisplayName("given existing topic when deleteTopicById is called then topic deleted")
    void givenExistingTopic_whenDeleteTopicById_thenTopicDeleted() {
        // given
        Long topicId = 1L;
        TopicModel existingTopicModel = new TopicModel(topicId, "Test Name", "Test Label", "Test Description");

        // mock
        when(topicRepository.findTopicById(topicId)).thenReturn(Optional.of(existingTopicModel));

        // when
        assertDoesNotThrow(() -> topicService.deleteTopicById(topicId),
                "deleteTopicById should not throw an exception for an existing topic");

        // then
        verify(topicRepository, times(1)).findTopicById(topicId);
        verify(topicRepository, times(1)).deleteTopic(topicId);
    }

    @Test
    @DisplayName("given non-existent topic when deleteTopicById is called then throws ResourceNotFoundException")
    void givenNonExistentTopic_whenDeleteTopicById_thenThrowsResourceNotFoundException() {
        // given
        Long nonExistentTopicId = 99L;

        // mock
        when(topicRepository.findTopicById(nonExistentTopicId)).thenReturn(Optional.empty());

        // when && then
        assertThrows(ResourceNotFoundException.class,
                () -> topicService.deleteTopicById(nonExistentTopicId),
                "deleteTopicById should throw a ResourceNotFoundException for a non-existent topic");
        verify(topicRepository, times(1)).findTopicById(nonExistentTopicId);
    }


}
