package com.reddot.ms.topic.controller.rest;

import com.reddot.ms.topic.data.dto.TopicDTO;
import com.reddot.ms.topic.exception.ResourceAlreadyExistException;
import com.reddot.ms.topic.exception.ResourceNotFoundException;
import com.reddot.ms.topic.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/topics")
public interface ITopicRestController {
    @GetMapping(value = "/", produces = {"application/json"})
    ResponseEntity<ApiResponse> getAllTopics();

    @GetMapping(value = "/{id}", produces = {"application/json"})
    ResponseEntity<ApiResponse> getTopicById(@PathVariable("id") Long id) throws ResourceNotFoundException;

    @PostMapping
    ResponseEntity<ApiResponse> createTopic(@RequestBody TopicDTO topicDTO) throws ResourceAlreadyExistException;

    @PutMapping("/{id}")
    ResponseEntity<ApiResponse> updateTopic(@PathVariable("id") Long id, @RequestBody TopicDTO topicDTO) throws ResourceNotFoundException;

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse> deleteTopic(@PathVariable("id") Long id) throws ResourceNotFoundException;
}