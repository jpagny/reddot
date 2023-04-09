package com.elysium.reddot.ms.topic.infrastructure.outbound.persistence;


import com.elysium.reddot.ms.topic.application.port.out.TopicRepositoryOutBound;
import com.elysium.reddot.ms.topic.application.service.dto.TopicDTO;
import com.elysium.reddot.ms.topic.infrastructure.mapper.TopicMapperInfrastructure;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.entity.TopicJpaEntity;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.repository.TopicJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TopicAdapter implements TopicRepositoryOutBound {

    private final TopicJpaRepository topicJpaRepository;

    public TopicAdapter(TopicJpaRepository topicJpaRepository) {
        this.topicJpaRepository = topicJpaRepository;
    }

    @Override
    public TopicDTO createTopic(TopicDTO topicDto) {
        TopicJpaEntity topicEntity = TopicMapperInfrastructure.toEntity(topicDto);
        TopicJpaEntity savedTopic = topicJpaRepository.save(topicEntity);
        return TopicMapperInfrastructure.toDTO(savedTopic);
    }

    @Override
    public Optional<TopicDTO> findTopicById(Long id) {
        return topicJpaRepository.findById(id)
                .map(TopicMapperInfrastructure::toDTO);
    }

    @Override
    public List<TopicDTO> findAllTopics() {
        return topicJpaRepository.findAll()
                .stream()
                .map(TopicMapperInfrastructure::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TopicDTO updateTopic(TopicDTO topicDto) {
        TopicJpaEntity topicEntity = TopicMapperInfrastructure.toEntity(topicDto);
        TopicJpaEntity updatedTopic = topicJpaRepository.save(topicEntity);
        return TopicMapperInfrastructure.toDTO(updatedTopic);
    }

    @Override
    public void deleteTopic(Long id) {
        topicJpaRepository.deleteById(id);
    }
}