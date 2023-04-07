package com.elysium.reddot.ms.topic.domain.service;

import com.elysium.reddot.ms.topic.domain.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.topic.domain.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;

import java.util.List;

public class TopicService implements ITopicService{

    public List<TopicModel> getAllTopics() {
        // TODO: add implementation to retrieve all topics
        return null;
    }

    public TopicModel getTopicById(Long id) throws ResourceNotFoundException {
        // TODO: add implementation to retrieve a topic by its ID
        return null;
    }

    public TopicModel createTopic(TopicModel topic) throws ResourceAlreadyExistException {
        // Validate input parameters
        if (topic == null) {
            throw new ResourceAlreadyExistException("","","");
        }
        if (topic.getName() == null || topic.getName().isEmpty()) {
            //throw new InvalidTopicException("Topic name is invalid");
        }

        // TODO: add implementation to create a new topic
        return null;
    }

    public TopicModel updateTopic(TopicModel topic) throws ResourceNotFoundException {
        // Validate input parameters
        if (topic == null) {
            throw new ResourceNotFoundException("","");
        }
        if (topic.getId() == null) {
            //throw new InvalidTopicException("Topic ID is null");
        }
        if (topic.getName() == null || topic.getName().isEmpty()) {
            //throw new InvalidTopicException("Topic name is invalid");
        }

        // TODO: add implementation to update an existing topic
        return null;
    }

    public void deleteTopicById(Long id) throws ResourceNotFoundException {
        // TODO: add implementation to delete a topic by its ID
    }
}
