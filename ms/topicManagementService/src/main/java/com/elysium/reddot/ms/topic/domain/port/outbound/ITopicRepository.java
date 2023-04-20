package com.elysium.reddot.ms.topic.domain.port.outbound;

import com.elysium.reddot.ms.topic.domain.model.TopicModel;

import java.util.List;
import java.util.Optional;


public interface ITopicRepository {
    TopicModel createTopic(TopicModel topicModel);

    Optional<TopicModel> findTopicById(Long id);

    Optional<TopicModel> findTopicByName(String name);

    List<TopicModel> findAllTopics();

    TopicModel updateTopic(TopicModel topicModel);

    void deleteTopic(Long id);
}
