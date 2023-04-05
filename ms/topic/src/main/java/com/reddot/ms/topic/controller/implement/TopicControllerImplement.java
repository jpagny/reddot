package com.reddot.ms.topic.controller.implement;

import com.reddot.ms.topic.controller.TopicController;
import com.reddot.ms.topic.dto.TopicDTO;
import com.reddot.ms.topic.exception.ResourceAlreadyExistException;
import com.reddot.ms.topic.exception.ResourceNotFoundException;
import com.reddot.ms.topic.response.ApiResponse;
import com.reddot.ms.topic.service.implement.TopicServiceImplement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("topicController")
@AllArgsConstructor
@Slf4j
public class TopicControllerImplement implements TopicController {

    private final TopicServiceImplement topicService;

    public ResponseEntity<ApiResponse> getAll() {
        log.debug("Endpoint: /topics. Retrieving all Topics");
        List<TopicDTO> topics = topicService.getAll();
        ApiResponse apiResponse = new ApiResponse("All topics retrieved successfully", topics);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> getTopicById(Long id) throws ResourceNotFoundException {
        log.debug("Endpoint: /topics/{}. Retrieving Topic with id: {}", id, id);
        TopicDTO topic = topicService.getById(id);
        ApiResponse apiResponse = new ApiResponse("Topic with id " + id + " retrieved successfully", topic);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> createTopic(TopicDTO topicDTO) throws ResourceAlreadyExistException {
        log.debug("Endpoint: /topics. Creating Topic with name: {}", topicDTO.getName());
        TopicDTO createdTopic = topicService.create(topicDTO);
        ApiResponse apiResponse = new ApiResponse("Topic with id " + createdTopic.getId() + " is created", createdTopic);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<ApiResponse> updateTopic(Long id, TopicDTO topicDTO) throws ResourceNotFoundException {
        log.debug("Endpoint: /topics/{}. Updating Topic with id: {}", id, id);
        TopicDTO updatedTopic = topicService.update(id, topicDTO);
        ApiResponse apiResponse = new ApiResponse("Topic with id " + updatedTopic.getId() + " is updated", updatedTopic);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deleteTopic(Long id) throws ResourceNotFoundException {
        log.debug("Endpoint: /topics/{}. Deleting Topic with id: {}", id, id);
        topicService.delete(id);
        ApiResponse apiResponse = new ApiResponse("Topic with id " + id + " is deleted");
        return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
    }

}
