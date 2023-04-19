package com.elysium.reddot.ms.board.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.entity.BoardJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardJpaRepository extends JpaRepository<BoardJpaEntity, Long> {
    Optional<BoardJpaEntity> findByName(String name);
}