package com.elysium.reddot.ms.topic.application.port.out;

import com.elysium.reddot.ms.topic.application.service.dto.TopicDTO;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;

import java.util.List;
import java.util.Optional;


public interface TopicRepositoryOutBound {
    TopicDTO createTopic(TopicDTO topicDto);
    Optional<TopicDTO> findTopicById(Long id);
    List<TopicDTO> findAllTopics();
    TopicDTO updateTopic(TopicDTO topicDto);
    void deleteTopic(Long id);
}
