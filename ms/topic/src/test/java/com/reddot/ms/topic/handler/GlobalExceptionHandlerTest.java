package com.reddot.ms.topic.handler;

import com.reddot.ms.topic.exception.ResourceAlreadyExistException;
import com.reddot.ms.topic.exception.ResourceNotFoundException;
import com.reddot.ms.topic.handle.GlobalExceptionHandler;
import com.reddot.ms.topic.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("given a ResourceAlreadyExistException, when handleResourceAlreadyExistException is called, then ApiResponse with CONFLICT status is returned")
    void givenResourceAlreadyExistException_whenHandleResourceAlreadyExistException_thenApiResponseWithConflictStatusIsReturned() {
        // arrange
        ResourceAlreadyExistException exception = new ResourceAlreadyExistException("topic", "name", "topic");

        // act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleResourceAlreadyExistException(exception);

        // assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("The topic with name 'topic' already exists.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @DisplayName("Given a ResourceNotFoundException, when handleResourceNotFoundException is called, then ApiResponse with NOT_FOUND status is returned")
    @Test
    void givenResourceNotFoundException_whenHandleResourceNotFoundException_thenApiResponseWithNotFoundStatusIsReturned() {
        // arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("topic", "1");

        // act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleResourceNotFoundException(exception);

        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("The topic with ID 1 does not exist.", Objects.requireNonNull(response.getBody()).getMessage());
    }

}