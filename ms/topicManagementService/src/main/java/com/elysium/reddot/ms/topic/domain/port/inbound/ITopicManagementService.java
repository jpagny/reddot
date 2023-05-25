package com.elysium.reddot.ms.topic.domain.port.inbound;

import com.elysium.reddot.ms.topic.domain.exception.type.FieldEmptyException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;

import java.util.List;

public interface ITopicManagementService {

    TopicModel getTopicById(Long id);

    List<TopicModel> getAllTopics();

    TopicModel createTopic(TopicModel topicModel) throws FieldEmptyException;

    TopicModel updateTopic(Long id, TopicModel topicModel);

    TopicModel deleteTopicById(Long id);
    
}
