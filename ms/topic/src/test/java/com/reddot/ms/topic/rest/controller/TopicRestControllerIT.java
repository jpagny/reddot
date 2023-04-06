package com.reddot.ms.topic.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reddot.ms.topic.data.dto.TopicDTO;
import com.reddot.ms.topic.response.ApiResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TopicRestControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("given get request to topics/, when getAllTopics called, then all topics are returned")
    void givenRequestToGetAllTopics_whenGetAllTopics_thenAllTopicsAreReturned() {
        // when
        ResponseEntity<ApiResult> response = restTemplate.getForEntity("/topics/", ApiResult.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("All topics retrieved successfully", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    @DisplayName("given get request to topics/xxx with an existing topic, when getTopicById called, then topic is returned")
    void givenTopicId_whenGetTopicById_thenTopicIsReturned() {
        long topicId = 1L;
        // arrange
        TopicDTO topicExpected = TopicDTO.builder()
                .id(topicId)
                .name("topic_1")
                .label("Topic 1")
                .description("Description 1")
                .build();

        // act
        ResponseEntity<ApiResult> response = restTemplate.getForEntity("/topics/" + topicId, ApiResult.class);
        ApiResult apiResult = response.getBody();
        assert apiResult != null;
        TopicDTO retrievedTopic = objectMapper.convertValue(apiResult.getData(), TopicDTO.class);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo("Topic with id " + topicExpected.getId() + " retrieved successfully");
        assertThat(retrievedTopic.getId()).isEqualTo(topicExpected.getId());
        assertThat(retrievedTopic.getName()).isEqualTo(topicExpected.getName());
        assertThat(retrievedTopic.getDescription()).isEqualTo(topicExpected.getDescription());
    }

    @Test
    @DisplayName("given post request to topics/ with a new topic, when createTopic called, then topic is created")
    void givenPostRequestToTopicsWithANewTopic_whenCreateTopicCalled_thenTopicIsCreated() {
        // arrange
        TopicDTO newTopic = TopicDTO.builder()
                .name("new_topic")
                .label("New topic")
                .description("New topic description")
                .build();

        // act
        ResponseEntity<ApiResult> response = restTemplate.postForEntity("/topics/", newTopic, ApiResult.class);
        ApiResult apiResult = response.getBody();
        assert apiResult != null;
        TopicDTO createdTopic = objectMapper.convertValue(apiResult.getData(), TopicDTO.class);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(apiResult.getMessage()).isEqualTo("Topic with id 3 is created");
        assertThat(createdTopic.getId()).isEqualTo(3);
        assertThat(createdTopic.getName()).isEqualTo("new_topic");
        assertThat(createdTopic.getLabel()).isEqualTo("New topic");
        assertThat(createdTopic.getDescription()).isEqualTo("New topic description");
    }

    @Test
    @DisplayName("given post request to topics/ with an existing topic, when createTopic called, then a conflict status is returned")
    void givenExistingTopic_whenCreateTopic_thenConflictStatus() {
        // arrange
        TopicDTO newTopicWithSameName = TopicDTO.builder()
                .name("topic_1")
                .label("Topic 1")
                .description("Topic description")
                .build();

        // act
        ResponseEntity<ApiResult> response = restTemplate.postForEntity("/topics/", newTopicWithSameName, ApiResult.class);
        ApiResult apiResult = response.getBody();
        assert apiResult != null;

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(apiResult.getMessage()).isEqualTo("The topic with name 'topic_1' already exists.");
    }

    @Test
    @DisplayName("given put request to topics/xxx with topic existing to update, when updateTopic is called, then the topic is updated and returned")
    void givenTopicWithId_whenUpdateTopic_thenTopicIsUpdatedAndReturned() {
        // arrange
        long topicId = 1L;
        TopicDTO updatedTopicDTO = TopicDTO.builder()
                .id(topicId)
                .name("Updated Topic Name")
                .label("updated")
                .description("Updated Topic Description")
                .build();

        // act
        HttpEntity<TopicDTO> request = new HttpEntity<>(updatedTopicDTO);
        ResponseEntity<ApiResult> response = restTemplate.exchange("/topics/" + topicId, HttpMethod.PUT, request, ApiResult.class);
        ApiResult apiResult = response.getBody();
        assert apiResult != null;
        TopicDTO updatedTopic = objectMapper.convertValue(apiResult.getData(), TopicDTO.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(topicId, updatedTopic.getId());
        assertEquals("Updated Topic Name", updatedTopic.getName());
        assertEquals("Updated Topic Description", updatedTopic.getDescription());
    }

    @DisplayName("given put request to topics/xxx with a non-existent topic ID, when updateTopic is called, then ResourceNotFoundException is thrown")
    @Test
    void givenNonExistentTopicId_whenUpdateTopic_thenResourceNotFoundExceptionIsThrown() {
        // arrange
        long nonExistentTopicId = 999L;
        TopicDTO updatedTopicDTO = TopicDTO.builder()
                .id(nonExistentTopicId)
                .name("Updated Topic Name")
                .label("Updated")
                .description("Updated Topic Description")
                .build();

        HttpEntity<TopicDTO> request = new HttpEntity<>(updatedTopicDTO);

        // act
        ResponseEntity<ApiResult> response = restTemplate.exchange("/topics/" + nonExistentTopicId, HttpMethod.PUT, request, ApiResult.class);
        ApiResult apiResult = response.getBody();
        assert apiResult != null;

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(apiResult.getMessage()).isEqualTo("The topic with ID 999 does not exist.");
    }

    @Test
    @DisplayName("given delete request to topics/xxx with an existing topic, when deleteTopic is called, then the topic is deleted")
    void givenExistingTopicId_whenDeleteTopic_thenTopicIsDeleted() {
        // arrange
        long existingTopicId = 1L;

        // act
        ResponseEntity<ApiResult> response = restTemplate.exchange("/topics/" + existingTopicId, HttpMethod.DELETE, HttpEntity.EMPTY, ApiResult.class);

        // assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("given delete request to topics/xxx with a non-existent topic, when deleteTopic is called, then ResourceNotFoundException is thrown")
    void givenNonExistentTopicId_whenDeleteTopic_thenResourceNotFoundExceptionIsThrown() {
        // arrange
        long nonExistentTopicId = 999L;

        // act
        ResponseEntity<ApiResult> response = restTemplate.exchange("/topics/" + nonExistentTopicId, HttpMethod.DELETE, HttpEntity.EMPTY, ApiResult.class);
        ApiResult apiResult = response.getBody();
        assert apiResult != null;

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(apiResult.getMessage()).isEqualTo("The topic with ID 999 does not exist.");
    }


}
