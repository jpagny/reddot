package com.reddot.ms.topic.rest.controller;

import com.reddot.ms.topic.controller.rest.implement.TopicRestControllerImpl;
import com.reddot.ms.topic.data.dto.TopicDTO;
import com.reddot.ms.topic.exception.ResourceAlreadyExistException;
import com.reddot.ms.topic.exception.ResourceNotFoundException;
import com.reddot.ms.topic.response.ApiResult;
import com.reddot.ms.topic.service.implement.TopicServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicRestControllerTest {

    @Mock
    private TopicServiceImpl topicService;

    @InjectMocks
    private TopicRestControllerImpl topicRestController;

    private List<TopicDTO> topicList;

    @BeforeEach
    void setUp() {
        topicList = Arrays.asList(
                TopicDTO.builder().id(1L).name("topic 1").label("Topic 1").description("Description 1").build(),
                TopicDTO.builder().id(2L).name("topic 2").label("Topic 2").description("Description 2").build(),
                TopicDTO.builder().id(3L).name("topic 3").label("Topic 3").description("Description 3").build()
        );
    }

    @Test
    @DisplayName("given topics, when getAllTopics called, then returns all topics with success")
    void givenTopics_whenGetAllTopicsCalled_thenReturnsAllTopicsWithSuccess() {
        // mock
        when(topicService.getAll()).thenReturn(topicList);

        // when
        ResponseEntity<ApiResult> response = topicRestController.getAllTopics();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("All topics retrieved successfully", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(topicList, response.getBody().getData());
    }

    @Test
    @DisplayName("given topicId, when getTopicById called, then returns topic with success")
    void givenTopicId_whenGetTopicByIdCalled_thenReturnsTopicWithSuccess() throws ResourceNotFoundException {
        // given
        Long topicId = 1L;

        // mock
        when(topicService.getById(topicId)).thenReturn(topicList.get(0));

        // when
        ResponseEntity<ApiResult> response = topicRestController.getTopicById(topicId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Topic with id " + topicId + " retrieved successfully", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(topicList.get(0), response.getBody().getData());
    }

    @Test
    @DisplayName("given non existent topicId, when getTopicById called, then throws ResourceNotFoundException")
    void givenNonexistentTopicId_whenGetTopicByIdCalled_thenThrowsResourceNotFoundException() throws ResourceNotFoundException {
        // given
        Long topicId = 999L;
        when(topicService.getById(topicId)).thenThrow(new ResourceNotFoundException("topic", String.valueOf(topicId)));

        // when && then
        assertThrows(ResourceNotFoundException.class, () -> topicRestController.getTopicById(topicId));
    }

    @Test
    @DisplayName("given topicDTO, when createTopic called, then topic is created with success")
    void givenTopicDTO_whenCreateTopicCalled_thenTopicIsCreatedWithSuccess() throws ResourceAlreadyExistException {
        // given
        TopicDTO newTopic = TopicDTO.builder()
                .name("topic2")
                .label("Topic 2")
                .description("Description 2")
                .build();
        TopicDTO createdTopic = TopicDTO.builder()
                .id(2L)
                .name("topic2")
                .label("Topic 2")
                .description("Description 2")
                .build();

        // mock
        when(topicService.create(newTopic)).thenReturn(createdTopic);

        // when
        ResponseEntity<ApiResult> response = topicRestController.addTopic(newTopic);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Topic with id " + createdTopic.getId() + " is created", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(createdTopic, response.getBody().getData());
    }

    @Test
    @DisplayName("given existing topicDTO, when createTopic called, then throws ResourceAlreadyExistException")
    void givenExistingTopicDTO_whenCreateTopicCalled_thenThrowsResourceAlreadyExistException() throws ResourceAlreadyExistException {
        // given
        TopicDTO existingTopicDTO = topicList.get(0);

        // mock
        when(topicService.create(existingTopicDTO)).thenThrow(new ResourceAlreadyExistException("topic", existingTopicDTO.getName(), "name"));

        // when && then
        assertThrows(ResourceAlreadyExistException.class, () -> topicRestController.addTopic(existingTopicDTO));
    }

    @Test
    @DisplayName("given topicId and topicDTO, when updateTopic called, then topic is updated with success")
    void givenTopicIdAndTopicDTO_whenUpdateTopicCalled_thenTopicIsUpdatedWithSuccess() throws ResourceNotFoundException {
        // given
        Long topicId = 1L;
        TopicDTO updatedTopicDTO = TopicDTO.builder()
                .id(topicId)
                .label("topic1")
                .name("Updated Topic 1")
                .description("Updated Description 1")
                .build();

        // mock
        when(topicService.update(topicId, updatedTopicDTO)).thenReturn(updatedTopicDTO);

        // when
        ResponseEntity<ApiResult> response = topicRestController.updateTopic(topicId, updatedTopicDTO);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Topic with id " + updatedTopicDTO.getId() + " is updated", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(updatedTopicDTO, response.getBody().getData());
    }

    @Test
    @DisplayName("given non existent topicId, when updateTopic called, then throws ResourceNotFoundException")
    void givenNonexistentTopicId_whenUpdateTopicCalled_thenThrowsResourceNotFoundException() throws ResourceNotFoundException {
        // given
        Long nonExistentTopicId = 999L;
        TopicDTO topicToUpdate = topicList.get(0);

        // mock
        when(topicService.update(nonExistentTopicId, topicToUpdate)).thenThrow(new ResourceNotFoundException("topic", String.valueOf(nonExistentTopicId)));

        // when && then
        assertThrows(ResourceNotFoundException.class, () -> topicRestController.updateTopic(nonExistentTopicId, topicToUpdate));
    }

    @Test
    @DisplayName("given topicId, when deleteTopic Called, then topic is deleted with success")
    void givenTopicId_whenDeleteTopicCalled_thenTopicIsDeletedWithSuccess() throws ResourceNotFoundException {
        // given
        Long topicId = 1L;

        // when
        ResponseEntity<ApiResult> response = topicRestController.deleteTopic(topicId);

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Topic with id " + topicId + " is deleted", Objects.requireNonNull(response.getBody()).getMessage());

        // mock && verify
        verify(topicService, times(1)).delete(topicId);
    }

    @Test
    @DisplayName("given non existent topicId, when delete topic called, then throws ResourceNotFoundException")
    void givenNonexistentTopicId_whenDeleteTopicCalled_thenThrowsResourceNotFoundException() throws ResourceNotFoundException {
        // given
        Long topicId = 999L;

        // mock
        doThrow(new ResourceNotFoundException("topic", String.valueOf(topicId))).when(topicService).delete(topicId);

        // when && then
        assertThrows(ResourceNotFoundException.class, () -> topicRestController.deleteTopic(topicId));
    }


}
