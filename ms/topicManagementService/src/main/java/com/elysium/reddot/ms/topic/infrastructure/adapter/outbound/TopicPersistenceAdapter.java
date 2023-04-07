package com.elysium.reddot.ms.topic.infrastructure.adapter.outbound;


import com.elysium.reddot.ms.topic.application.port.out.TopicRepository;
import com.elysium.reddot.ms.topic.application.service.dto.TopicDTO;
import com.elysium.reddot.ms.topic.infrastructure.adapter.outbound.persistence.TopicJpaEntity;
import com.elysium.reddot.ms.topic.infrastructure.adapter.outbound.persistence.TopicJpaRepository;
import com.elysium.reddot.ms.topic.infrastructure.mapper.TopicEntityDTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TopicPersistenceAdapter implements TopicRepository {

    private final TopicJpaRepository topicJpaRepository;

    public TopicPersistenceAdapter(TopicJpaRepository topicJpaRepository) {
        this.topicJpaRepository = topicJpaRepository;
    }

    @Override
    public List<TopicDTO> findAll() {
        return topicJpaRepository.findAll().stream()
                .map(TopicEntityDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TopicDTO> findById(Long id) {
        return topicJpaRepository.findById(id).map(TopicEntityDTOMapper::toDTO);
    }

    @Override
    public TopicDTO save(TopicDTO topicDTO) {
        TopicJpaEntity topicJpaEntity = TopicEntityDTOMapper.toEntity(topicDTO);
        TopicJpaEntity savedTopicJpaEntity = topicJpaRepository.save(topicJpaEntity);
        return TopicEntityDTOMapper.toDTO(savedTopicJpaEntity);
    }

    @Override
    public void deleteById(Long id) {
        topicJpaRepository.deleteById(id);
    }
}