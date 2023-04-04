package com.reddot.ms.topic.controller;

import com.reddot.ms.topic.dto.TopicDTO;
import com.reddot.ms.topic.exception.ResourceNotFoundException;
import com.reddot.ms.topic.service.implement.TopicServiceImplement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/topic")
@Slf4j
public class TopicController {

    private final TopicServiceImplement topicService;

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<TopicDTO> getTopicById(@PathVariable("id") Long id) {
        log.debug("Trying to fetch topic with id '{}'", id);

        try {

            TopicDTO topic = topicService.getById(id);

            log.info("Successfully fetched topic with id '{}', name '{}', description '{}'",
                    topic.getId(), topic.getName(), topic.getDescription());

            return new ResponseEntity<>(topic, HttpStatus.OK);

        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }


}
