package com.reddot.ms.topic.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reddot.ms.topic.data.dto.TopicDTO;
import com.reddot.ms.topic.response.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class TopicRestControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("given get request to topics/, when getAllTopics called, then all topics are returned")
    void givenRequestToGetAllTopics_whenGetAllTopics_thenAllTopicsAreReturned() {
        // when
        ResponseEntity<ApiResponse> response = restTemplate.getForEntity("/topics/", ApiResponse.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("All topics retrieved successfully", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    @DisplayName("given get request to topics/1, when getTopicById called, then topic is returned")
    void givenTopicId_whenGetTopicById_thenTopicIsReturned() {
        // arrange
        TopicDTO topicExpected = TopicDTO.builder()
                .id(1L)
                .name("topic_1")
                .label("Topic 1")
                .description("Description 1")
                .build();

        // act
        ResponseEntity<ApiResponse> response = restTemplate.getForEntity("/topics/1", ApiResponse.class);
        TopicDTO retrievedTopic = objectMapper.convertValue(Objects.requireNonNull(response.getBody()).getData(), TopicDTO.class);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo("Topic with id " + topicExpected.getId() + " retrieved successfully");
        assertThat(retrievedTopic.getId()).isEqualTo(topicExpected.getId());
        assertThat(retrievedTopic.getName()).isEqualTo(topicExpected.getName());
        assertThat(retrievedTopic.getDescription()).isEqualTo(topicExpected.getDescription());
    }

    @Test
    @DisplayName("given post request to topics/ with a new topic, when createTopic called, then topic is created")
    public void givenPostRequestToTopicsWithANewTopic_whenCreateTopicCalled_thenTopicIsCreated() {
        // arrange
        TopicDTO newTopic = TopicDTO.builder()
                .name("new_topic")
                .label("New topic")
                .description("New topic description")
                .build();

        // act
        ResponseEntity<ApiResponse> response = restTemplate.postForEntity("/topics/", newTopic, ApiResponse.class);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ApiResponse apiResponse = response.getBody();
        assertThat(apiResponse).isNotNull();
        assert apiResponse != null;
        assertThat(apiResponse.getMessage()).isEqualTo("Topic with id 3 is created");
        TopicDTO createdTopic = objectMapper.convertValue(apiResponse.getData(), TopicDTO.class);
        assertThat(createdTopic.getId()).isEqualTo(3);
        assertThat(createdTopic.getName()).isEqualTo("new_topic");
        assertThat(createdTopic.getLabel()).isEqualTo("New topic");
        assertThat(createdTopic.getDescription()).isEqualTo("New topic description");

    }

    @Test
    @DisplayName("given post request to topics/ with an existing topic, when createTopic called, then a conflict status is returned")
    public void givenExistingTopic_whenCreateTopic_thenConflictStatus() {
        // arrange
        TopicDTO newTopicWithSameName = TopicDTO.builder()
                .name("topic_1")
                .label("Topic 1")
                .description("Topic description")
                .build();

        // act
        ResponseEntity<ApiResponse> response = restTemplate.postForEntity("/topics", newTopicWithSameName, ApiResponse.class);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        ApiResponse apiResponse = response.getBody();
        assertThat(apiResponse).isNotNull();
        assert apiResponse != null;
        assertThat(apiResponse.getMessage()).isEqualTo("The topic with topic_1 'name' already exists");
    }


}
