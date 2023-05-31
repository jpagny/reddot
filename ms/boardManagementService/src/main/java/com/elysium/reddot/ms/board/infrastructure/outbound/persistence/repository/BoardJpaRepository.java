package com.elysium.reddot.ms.board.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.entity.BoardJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing BoardJpaEntity entities in the database.
 */
@Repository
public interface BoardJpaRepository extends JpaRepository<BoardJpaEntity, Long> {

    /**
     * Retrieves a board entity by its name and by topicId.
     *
     * @param name the name of the board
     * @param topicId the id of the topic
     * @return an optional containing the board entity, or an empty optional if not found
     */
    @Query("SELECT m FROM BoardJpaEntity m WHERE m.name = :name AND m.topicId = :topicId")
    Optional<BoardJpaEntity> findFirstByNameAndTopicId(@Param("name") String name, @Param("topicId") Long topicId);

}