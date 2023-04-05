package com.reddot.ms.topic.controller;

import com.reddot.ms.topic.dto.TopicDTO;
import com.reddot.ms.topic.exception.ResourceNotFoundException;
import com.reddot.ms.topic.service.implement.TopicServiceImplement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.model.RouteDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/topic")
@Slf4j
public class TopicController {

    @Autowired
    CamelContext camelContext;

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

    @GetMapping(value = "/", produces = {"application/json"})
    public ResponseEntity<List<TopicDTO>> getAll() {
            List<TopicDTO> topics = topicService.getAll();
            return new ResponseEntity<>(topics, HttpStatus.OK);
    }



    @GetMapping("/routes")
    public List<String> getRoutes() throws Exception {
        List<String> routes = new ArrayList<>();
        for (Route route : camelContext.getRoutes()) {
            routes.add(route.getId());
        }
        return routes;
    }



}
