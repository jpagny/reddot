package com.elysium.reddot.ms.board.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.entity.BoardJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing BoardJpaEntity entities in the database.
 */
@Repository
public interface BoardJpaRepository extends JpaRepository<BoardJpaEntity, Long> {

    /**
     * Retrieves a board entity by its name.
     *
     * @param name the name of the board
     * @return an optional containing the board entity, or an empty optional if not found
     */
    Optional<BoardJpaEntity> findByName(String name);

}