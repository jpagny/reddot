package com.reddot.ms.topic.controller.rest.implement;

import com.reddot.ms.topic.controller.rest.ITopicRestController;
import com.reddot.ms.topic.data.dto.TopicDTO;
import com.reddot.ms.topic.exception.ResourceAlreadyExistException;
import com.reddot.ms.topic.exception.ResourceNotFoundException;
import com.reddot.ms.topic.response.ApiResult;
import com.reddot.ms.topic.service.ITopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("topicRestController")
@AllArgsConstructor
@Slf4j
public class TopicRestControllerImpl implements ITopicRestController {

    private static final String ENDPOINT = "/topics";
    private static final String MESSAGE_TOPIC_WITH_ID = "Topic with id ";

    private final ITopicService topicService;

    @Operation(summary = "Retrieve all topics from the data source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all topics",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TopicDTO[].class))})
    })
    @GetMapping(value = "/", produces = {"application/json"})
    public ResponseEntity<ApiResult> getAllTopics() {
        log.debug("Endpoint: {}. Retrieving all Topics", ENDPOINT);
        List<TopicDTO> topics = topicService.getAll();
        ApiResult apiResult = new ApiResult("All topics retrieved successfully", topics);
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<ApiResult> getTopicById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        log.debug("Endpoint: {}/{}. Retrieving Topic with id: {}", ENDPOINT, id, id);
        TopicDTO topic = topicService.getById(id);
        ApiResult apiResult = new ApiResult(MESSAGE_TOPIC_WITH_ID + id + " retrieved successfully", topic);
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }

    @PostMapping(value = "/", produces = {"application/json"})
    public ResponseEntity<ApiResult> addTopic(@RequestBody TopicDTO topicDTO) throws ResourceAlreadyExistException {
        log.debug("Endpoint: {}. Creating Topic with name: {}", ENDPOINT, topicDTO.getName());
        TopicDTO createdTopic = topicService.create(topicDTO);
        ApiResult apiResult = new ApiResult(MESSAGE_TOPIC_WITH_ID + createdTopic.getId() + " is created", createdTopic);
        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<ApiResult> updateTopic(@PathVariable("id") Long id, @RequestBody TopicDTO topicDTO) throws ResourceNotFoundException {
        log.debug("Endpoint: {}/{}. Updating Topic with id: {}", ENDPOINT, id, id);
        TopicDTO updatedTopic = topicService.update(id, topicDTO);
        ApiResult apiResult = new ApiResult(MESSAGE_TOPIC_WITH_ID + updatedTopic.getId() + " is updated", updatedTopic);
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<ApiResult> deleteTopic(@PathVariable("id") Long id) throws ResourceNotFoundException {
        log.debug("Endpoint: {}/{}. Deleting Topic with id: {}", ENDPOINT, id, id);
        topicService.delete(id);
        ApiResult apiResult = new ApiResult(MESSAGE_TOPIC_WITH_ID + id + " is deleted");
        return new ResponseEntity<>(apiResult, HttpStatus.NO_CONTENT);
    }

}
