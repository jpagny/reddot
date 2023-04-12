package com.elysium.reddot.ms.topic.application.service;

import com.elysium.reddot.ms.topic.application.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.application.service.dto.TopicDTO;
import com.elysium.reddot.ms.topic.domain.exception.LabelEmptyException;

import java.util.List;
import java.util.Optional;

public interface TopicApplicationService {

    TopicDTO getTopicById(Long id) throws ResourceNotFoundException;

    List<TopicDTO> getAllTopics();

    TopicDTO createTopic(TopicDTO topicDto) throws LabelEmptyException;

    TopicDTO updateTopic(Long id, TopicDTO topicDto);

    void deleteTopicById(Long id) throws ResourceNotFoundException;
}
