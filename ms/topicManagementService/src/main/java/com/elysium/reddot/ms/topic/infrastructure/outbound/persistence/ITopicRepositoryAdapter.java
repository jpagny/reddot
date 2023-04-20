package com.elysium.reddot.ms.topic.infrastructure.outbound.persistence;


import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.domain.port.out.ITopicRepository;
import com.elysium.reddot.ms.topic.infrastructure.mapper.TopicPersistenceMapper;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.entity.TopicJpaEntity;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.repository.TopicJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ITopicRepositoryAdapter implements ITopicRepository {

    private final TopicJpaRepository topicJpaRepository;

    public ITopicRepositoryAdapter(TopicJpaRepository topicJpaRepository) {
        this.topicJpaRepository = topicJpaRepository;
    }

    @Override
    public TopicModel createTopic(TopicModel topicModel) {
        TopicJpaEntity topicEntity = TopicPersistenceMapper.toEntity(topicModel);
        TopicJpaEntity savedTopic = topicJpaRepository.save(topicEntity);
        return TopicPersistenceMapper.toModel(savedTopic);
    }

    @Override
    public Optional<TopicModel> findTopicById(Long id) {
        return topicJpaRepository.findById(id)
                .map(TopicPersistenceMapper::toModel);
    }

    @Override
    public Optional<TopicModel> findTopicByName(String name) {
        return topicJpaRepository.findByName(name)
                .map(TopicPersistenceMapper::toModel);
    }

    @Override
    public List<TopicModel> findAllTopics() {
        return topicJpaRepository.findAll()
                .stream()
                .map(TopicPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public TopicModel updateTopic(TopicModel topicModel) {
        TopicJpaEntity topicEntity = TopicPersistenceMapper.toEntity(topicModel);
        TopicJpaEntity updatedTopic = topicJpaRepository.save(topicEntity);
        return TopicPersistenceMapper.toModel(updatedTopic);
    }

    @Override
    public void deleteTopic(Long id) {
        topicJpaRepository.deleteById(id);
    }
}
