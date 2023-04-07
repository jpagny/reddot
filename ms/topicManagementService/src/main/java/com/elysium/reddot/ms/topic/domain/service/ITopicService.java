package com.elysium.reddot.ms.topic.domain.service;

import com.elysium.reddot.ms.topic.domain.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.topic.domain.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;

import java.util.List;

public interface ITopicService {

    List<TopicModel> getAllTopics();

    TopicModel getTopicById(Long id) throws ResourceNotFoundException;

    TopicModel createTopic(TopicModel topic) throws ResourceAlreadyExistException;

    TopicModel updateTopic(TopicModel topic) throws ResourceNotFoundException;

    void deleteTopicById(Long id) throws ResourceNotFoundException;

}