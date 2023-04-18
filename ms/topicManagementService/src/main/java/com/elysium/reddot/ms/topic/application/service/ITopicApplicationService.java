package com.elysium.reddot.ms.topic.application.service;

import com.elysium.reddot.ms.topic.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.domain.exception.FieldEmptyException;

import java.util.List;

public interface ITopicApplicationService {

    TopicDTO getTopicById(Long id) throws ResourceNotFoundException;

    List<TopicDTO> getAllTopics();

    TopicDTO createTopic(TopicDTO topicDto) throws FieldEmptyException;

    TopicDTO updateTopic(Long id, TopicDTO topicDto);

    void deleteTopicById(Long id) throws ResourceNotFoundException;
}
