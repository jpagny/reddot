package com.elysium.reddot.ms.topic.application.port.out;

import com.elysium.reddot.ms.topic.application.service.dto.TopicDTO;

import java.util.List;
import java.util.Optional;

public interface TopicRepository {

    TopicDTO save(TopicDTO topicDTO);

    Optional<TopicDTO> findById(Long id);

    List<TopicDTO> findAll();

    void deleteById(Long id);

}