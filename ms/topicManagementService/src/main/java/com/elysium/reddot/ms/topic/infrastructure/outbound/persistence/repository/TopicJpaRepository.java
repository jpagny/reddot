package com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.entity.TopicJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on TopicJpaEntity.
 * It extends the JpaRepository interface from Spring Data JPA.
 */
@Repository
public interface TopicJpaRepository extends JpaRepository<TopicJpaEntity, Long> {

    /**
     * Retrieves a topic entity by its name.
     *
     * @param name The name of the topic.
     * @return An Optional containing the topic entity, or an empty Optional if not found.
     */
    Optional<TopicJpaEntity> findByName(String name);
}