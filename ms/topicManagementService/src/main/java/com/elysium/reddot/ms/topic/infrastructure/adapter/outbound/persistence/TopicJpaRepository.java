package com.elysium.reddot.ms.topic.infrastructure.adapter.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicJpaRepository extends JpaRepository<TopicJpaEntity, Long> {
    Optional<TopicJpaEntity> findByName(String name);
}