package com.elysium.reddot.ms.thread.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.thread.infrastructure.outbound.persistence.entity.ThreadJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The ThreadJpaRepository interface is a Spring Data JPA repository interface for the ThreadJpaEntity entity.
 * It provides methods to perform CRUD operations and custom queries on the "threads" table.
 */
@Repository
public interface ThreadJpaRepository extends JpaRepository<ThreadJpaEntity, Long> {

    /**
     * Retrieves the first thread entity with the specified name and boardId.
     *
     * @param name    The name of the thread.
     * @param boardId The ID of the board.
     * @return An Optional containing the matching ThreadJpaEntity, or an empty Optional if not found.
     */
    @Query("SELECT m FROM ThreadJpaEntity m WHERE m.name = :name AND m.boardId = :boardId")
    Optional<ThreadJpaEntity> findFirstByNameAndBoardId(@Param("name") String name, @Param("boardId") Long boardId);

}