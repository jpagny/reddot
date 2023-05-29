package com.elysium.reddot.ms.message.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.message.infrastructure.outbound.persistence.entity.MessageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link MessageJpaEntity} instances in the database.
 */
@Repository
public interface MessageJpaRepository extends JpaRepository<MessageJpaEntity, Long> {

    /**
     * Finds the first message with the specified content and thread ID, ordered by creation timestamp.
     *
     * @param content  the content of the message
     * @param threadId the ID of the thread
     * @return an optional containing the first message found, or an empty optional if not found
     */
    @Query("SELECT m FROM MessageJpaEntity m WHERE m.content = :content AND m.threadId = :threadId ORDER BY m.createdAt")
    Optional<MessageJpaEntity> findFirstByContentAndThreadId(@Param("content") String content, @Param("threadId") Long threadId);

    /**
     * Finds all messages created by the specified user within the given date range.
     *
     * @param userId     the ID of the user
     * @param startDate  the start date of the range
     * @param endDate    the end date of the range
     * @return a list of messages created by the user within the specified date range
     */
    @Query("SELECT m FROM MessageJpaEntity m WHERE m.userId = :userId AND m.createdAt BETWEEN :startDate AND :endDate")
    List<MessageJpaEntity> findMessagesByUserIdAndDateRange(@Param("userId") String userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}