package com.elysium.reddot.ms.topic.application.port.out;

import com.elysium.reddot.ms.topic.application.service.dto.TopicDTO;
import com.elysium.reddot.ms.topic.infrastructure.adapter.outbound.persistence.TopicJpaEntity;

import java.util.List;
import java.util.Optional;

public interface TopicRepository {

    TopicDTO save(TopicDTO topicDTO);

    Optional<TopicDTO> findById(Long id);

    Optional<TopicDTO> findByName(String name);

    List<TopicDTO> findAll();

    void deleteById(Long id);

}