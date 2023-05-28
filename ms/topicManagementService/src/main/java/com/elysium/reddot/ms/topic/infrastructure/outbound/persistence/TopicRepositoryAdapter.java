package com.elysium.reddot.ms.topic.infrastructure.outbound.persistence;


import com.elysium.reddot.ms.topic.application.data.mapper.TopicJpaTopicModelMapper;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.domain.port.outbound.ITopicRepository;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.entity.TopicJpaEntity;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.repository.TopicJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Component that adapts the TopicJpaRepository to the ITopicRepository interface.
 */
@Component
public class TopicRepositoryAdapter implements ITopicRepository {

    private final TopicJpaRepository topicJpaRepository;

    public TopicRepositoryAdapter(TopicJpaRepository topicJpaRepository) {
        this.topicJpaRepository = topicJpaRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TopicModel createTopic(TopicModel topicModel) {
        TopicJpaEntity topicEntity = TopicJpaTopicModelMapper.toEntity(topicModel);
        TopicJpaEntity savedTopic = topicJpaRepository.save(topicEntity);
        return TopicJpaTopicModelMapper.toModel(savedTopic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<TopicModel> findTopicById(Long id) {
        return topicJpaRepository.findById(id)
                .map(TopicJpaTopicModelMapper::toModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<TopicModel> findTopicByName(String name) {
        return topicJpaRepository.findByName(name)
                .map(TopicJpaTopicModelMapper::toModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicModel> findAllTopics() {
        return topicJpaRepository.findAll()
                .stream()
                .map(TopicJpaTopicModelMapper::toModel)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TopicModel updateTopic(TopicModel topicModel) {
        TopicJpaEntity topicEntity = TopicJpaTopicModelMapper.toEntity(topicModel);
        TopicJpaEntity updatedTopic = topicJpaRepository.save(topicEntity);
        return TopicJpaTopicModelMapper.toModel(updatedTopic);
    }

}
