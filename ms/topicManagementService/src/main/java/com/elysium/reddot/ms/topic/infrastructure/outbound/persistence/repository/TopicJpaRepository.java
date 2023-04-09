package com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.entity.TopicJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicJpaRepository extends JpaRepository<TopicJpaEntity, Long> {
    Optional<TopicModel> findByName(String name);
}