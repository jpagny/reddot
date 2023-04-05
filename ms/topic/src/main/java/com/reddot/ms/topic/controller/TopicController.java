package com.reddot.ms.topic.controller;

import com.reddot.ms.topic.dto.TopicDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/topics")
public interface TopicController {
    @GetMapping(value = "/", produces = {"application/json"})
    ResponseEntity<List<TopicDTO>> getAll();

    @GetMapping(value = "/{id}", produces = {"application/json"})
    ResponseEntity<TopicDTO> getTopicById(@PathVariable("id") Long id);

}
