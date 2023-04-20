package com.elysium.reddot.ms.topic.domain.port.in;

import com.elysium.reddot.ms.topic.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;

import java.util.List;

public interface ITopicManagementService {

    TopicModel getTopicById(Long id) throws ResourceNotFoundException;

    List<TopicModel> getAllTopics();

    TopicModel createTopic(TopicModel topicModel) throws FieldEmptyException;

    TopicModel updateTopic(Long id, TopicModel topicModel);

    TopicModel deleteTopicById(Long id) throws ResourceNotFoundException;

}
