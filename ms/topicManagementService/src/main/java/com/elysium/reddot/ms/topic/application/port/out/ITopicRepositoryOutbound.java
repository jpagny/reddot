package com.elysium.reddot.ms.topic.application.port.out;

import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;

import java.util.List;
import java.util.Optional;


public interface ITopicRepositoryOutbound {
    TopicDTO createTopic(TopicDTO topicDto);

    Optional<TopicDTO> findTopicById(Long id);

    Optional<TopicDTO> findTopicByName(String name);

    List<TopicDTO> findAllTopics();

    TopicDTO updateTopic(TopicDTO topicDto);

    void deleteTopic(Long id);
}
