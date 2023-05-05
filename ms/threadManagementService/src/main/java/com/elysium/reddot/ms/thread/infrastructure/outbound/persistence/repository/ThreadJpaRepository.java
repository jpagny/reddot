package com.elysium.reddot.ms.thread.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.thread.infrastructure.outbound.persistence.entity.ThreadJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThreadJpaRepository extends JpaRepository<ThreadJpaEntity, Long> {
    Optional<ThreadJpaEntity> findByName(String name);
}