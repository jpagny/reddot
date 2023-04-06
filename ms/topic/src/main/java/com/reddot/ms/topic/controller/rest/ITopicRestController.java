package com.reddot.ms.topic.controller.rest;

import com.reddot.ms.topic.data.dto.TopicDTO;
import com.reddot.ms.topic.exception.ResourceAlreadyExistException;
import com.reddot.ms.topic.exception.ResourceNotFoundException;
import com.reddot.ms.topic.response.ApiResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/topics")
public interface ITopicRestController {

    ResponseEntity<ApiResult> getAllTopics();

    ResponseEntity<ApiResult> getTopicById(Long id) throws ResourceNotFoundException;

    ResponseEntity<ApiResult> addTopic(TopicDTO topicDTO) throws ResourceAlreadyExistException;

    ResponseEntity<ApiResult> updateTopic(Long id, TopicDTO topicDTO) throws ResourceNotFoundException;


    ResponseEntity<ApiResult> deleteTopic(@PathVariable("id") Long id) throws ResourceNotFoundException;
}