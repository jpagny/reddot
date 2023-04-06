package com.reddot.ms.topic.rest.controller;

import com.reddot.ms.topic.response.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TopicRestControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("given request to /, when getAllTopics called, then all topics are returned")
    void givenRequestToGetAllTopics_whenGetAllTopics_thenAllTopicsAreReturned() {
        // when
        ResponseEntity<ApiResponse> response = restTemplate.getForEntity("/topics/", ApiResponse.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("All topics retrieved successfully", Objects.requireNonNull(response.getBody()).getMessage());
    }

}
