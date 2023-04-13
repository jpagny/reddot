package com.elysium.reddot.ms.topic.application.service;

import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.exception.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.topic.application.exception.exception.ResourceBadValueException;
import com.elysium.reddot.ms.topic.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.domain.service.TopicDomainService;
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
public class TopicApplicationServiceTest {


    private TopicApplicationServiceImpl topicService;
    @Mock
    private TopicRepositoryAdapter topicRepository;

    @BeforeEach
    void setUp() {
        TopicDomainService topicDomainService = new TopicDomainService();
        topicService = new TopicApplicationServiceImpl(topicDomainService, topicRepository);
    }

    @Test
    void givenValidId_whenGetTopicById_thenReturnsTopic() {
        // given
        Long validId = 1L;
        TopicDTO expectedTopic = new TopicDTO(1L, "test", "Test Name", "Test Description");

        // mock
        when(topicRepository.findTopicById(validId)).thenReturn(Optional.of(expectedTopic));

        // when
        TopicDTO actualTopic = topicService.getTopicById(validId);

        // then
        assertEquals(expectedTopic, actualTopic);
        verify(topicRepository, times(1)).findTopicById(validId);
    }

    @Test
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
    @DisplayName("given existing topics, when getAll, then returns List of topicDTO")
    void givenExistingTopics_whenGetAll_thenReturnsListOfTopicDTOs() {
        // given
        TopicDTO topic1 = new TopicDTO(1L, "test1", "Test Name 1", "Test Description 1");
        TopicDTO topic2 = new TopicDTO(2L, "test2", "Test Name 2", "Test Description 2");

        // mock
        List<TopicDTO> expectedTopics = Arrays.asList(topic1, topic2);
        when(topicService.getAllTopics()).thenReturn(expectedTopics);

        // when
        List<TopicDTO> actualTopics = topicService.getAllTopics();

        // then
        assertEquals(expectedTopics, actualTopics, "The returned topic list should match the expected topic list");
        verify(topicRepository, times(1)).findAllTopics();
    }

    @Test
    void givenValidTopicDTO_whenCreateTopic_thenTopicCreated() {
        // given
        TopicDTO topicToCreateDTO = new TopicDTO(null, "test", "Test Label", "Test Description");
        TopicDTO createdTopicDTO = new TopicDTO(1L, "test 2", "Test Label", "Test Description");

        // mock
        when(topicRepository.findTopicByName(topicToCreateDTO.getName())).thenReturn(Optional.empty());
        when(topicRepository.createTopic(topicToCreateDTO)).thenReturn(createdTopicDTO);

        // when
        TopicDTO actualTopicDTO = topicService.createTopic(topicToCreateDTO);

        // then
        assertEquals(createdTopicDTO, actualTopicDTO, "The created topic should match the expected topic");
        verify(topicRepository, times(1)).findTopicByName(topicToCreateDTO.getName());
        verify(topicRepository, times(1)).createTopic(topicToCreateDTO);
    }

    @Test
    void givenExistingTopic_whenCreateTopic_thenThrowsResourceAlreadyExistException() {
        // given
        TopicDTO existingTopicDTO = new TopicDTO(1L, "Test Name", "Test Label", "Test Description");

        // mock
        when(topicRepository.findTopicByName(existingTopicDTO.getName())).thenReturn(Optional.of(existingTopicDTO));

        // when && then
        assertThrows(ResourceAlreadyExistException.class,
                () -> topicService.createTopic(existingTopicDTO),
                "createTopic should throw a ResourceAlreadyExistException for an existing topic");
        verify(topicRepository, times(1)).findTopicByName(existingTopicDTO.getName());
    }

    @Test
    void givenInvalidTopicDTO_whenCreateTopic_thenThrowsResourceBadValueException() {
        // given
        TopicDTO invalidTopicDTO = new TopicDTO(1L, "", "Invalid Label", "Invalid Description");

        // mock
        when(topicRepository.findTopicByName(invalidTopicDTO.getName())).thenReturn(Optional.empty());

        // when && then
        assertThrows(ResourceBadValueException.class,
                () -> topicService.createTopic(invalidTopicDTO),
                "createTopic should throw a ResourceBadValueException for an invalid topic");
        verify(topicRepository, times(1)).findTopicByName(invalidTopicDTO.getName());
    }

    @Test
    void givenValidTopicDTO_whenUpdateTopic_thenTopicUpdated() {
        // given
        Long topicId = 1L;
        TopicDTO existingTopicDTO = new TopicDTO(topicId, "Old Name", "OldLabel", "Old Description");
        TopicDTO topicToUpdateDTO = new TopicDTO(topicId, "Old Name", "NewLabel", "NewDescription");
        TopicDTO updatedTopicDTO = new TopicDTO(topicId, "Old Name", "NewLabel", "NewDescription");

        // mock
        when(topicRepository.findTopicById(topicId)).thenReturn(Optional.of(existingTopicDTO));
        when(topicRepository.updateTopic(updatedTopicDTO)).thenReturn(updatedTopicDTO);

        // when
        TopicDTO actualTopicDTO = topicService.updateTopic(1L, topicToUpdateDTO);

        // then
        assertEquals(updatedTopicDTO, actualTopicDTO, "The updated topic should match the expected topic");
        verify(topicRepository, times(1)).findTopicById(topicId);
        verify(topicRepository, times(1)).updateTopic(updatedTopicDTO);
    }

    @Test
    void givenNonExistentTopic_whenUpdateTopic_thenThrowsResourceNotFoundException() {
        // given
        Long nonExistentTopicId = 99L;
        TopicDTO topicToUpdateDTO = new TopicDTO(nonExistentTopicId, "New Name", "New Label", "New Description");

        // mock
        when(topicRepository.findTopicById(nonExistentTopicId)).thenReturn(Optional.empty());

        // then && when
        assertThrows(ResourceNotFoundException.class,
                () -> topicService.updateTopic(nonExistentTopicId, topicToUpdateDTO),
                "updateTopic should throw a ResourceNotFoundException for a non-existent topic");
        verify(topicRepository, times(1)).findTopicById(nonExistentTopicId);
    }

    @Test
    void givenInvalidTopicDTO_whenUpdateTopic_thenThrowsResourceBadValueException() {
        // given
        Long topicId = 1L;
        TopicDTO existingTopicDTO = new TopicDTO(topicId, "Old Name", "Old Label", "Old Description");
        TopicDTO invalidTopicToUpdateDTO = new TopicDTO(topicId, "Invalid Name", "", "Invalid Description");

        // mock
        when(topicRepository.findTopicById(topicId)).thenReturn(Optional.of(existingTopicDTO));

        // when && then
        assertThrows(ResourceBadValueException.class,
                () -> topicService.updateTopic
                        (topicId, invalidTopicToUpdateDTO),
                "updateTopic should throw a ResourceBadValueException for an invalid topic");
        verify(topicRepository, times(1)).findTopicById(topicId);
    }

    @Test
    void givenExistingTopic_whenDeleteTopicById_thenTopicDeleted() {
        // given
        Long topicId = 1L;
        TopicDTO existingTopicDTO = new TopicDTO(topicId, "Test Name", "Test Label", "Test Description");

        // mock
        when(topicRepository.findTopicById(topicId)).thenReturn(Optional.of(existingTopicDTO));

        // when
        assertDoesNotThrow(() -> topicService.deleteTopicById(topicId),
                "deleteTopicById should not throw an exception for an existing topic");

        // then
        verify(topicRepository, times(1)).findTopicById(topicId);
        verify(topicRepository, times(1)).deleteTopic(topicId);
    }

    @Test
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
