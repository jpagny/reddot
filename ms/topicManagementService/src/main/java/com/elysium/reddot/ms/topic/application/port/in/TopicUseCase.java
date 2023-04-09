package com.elysium.reddot.ms.topic.application.port.in;

import com.elysium.reddot.ms.topic.application.service.dto.TopicDTO;
import com.elysium.reddot.ms.topic.domain.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.topic.domain.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;

import java.util.List;
import java.util.Optional;

public interface TopicUseCase {

    List<TopicDTO> getAllTopics();

    Optional<TopicDTO> getTopicById(Long id) throws ResourceNotFoundException;

    TopicDTO createTopic(TopicDTO topic) throws ResourceAlreadyExistException;

    TopicDTO updateTopic(Long id, TopicDTO topic) throws ResourceNotFoundException;

    void deleteTopicById(Long id) throws ResourceNotFoundException;

}
