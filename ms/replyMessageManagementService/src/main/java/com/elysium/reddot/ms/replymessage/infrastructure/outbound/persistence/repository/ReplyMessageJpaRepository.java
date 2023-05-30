package com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.entity.ReplyMessageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyMessageJpaRepository extends JpaRepository<ReplyMessageJpaEntity, Long> {

    /**
     * Finds the first reply message by its content and parent message ID, ordered by creation date.
     *
     * @param content        the content of the reply message
     * @param parentMessageId the ID of the parent message
     * @return an optional containing the first reply message with the given content and parent message ID, or empty if not found
     */
    @Query("SELECT m FROM ReplyMessageJpaEntity m WHERE m.content = :content AND m.parentMessageId = :parentMessageId ORDER BY m.createdAt")
    Optional<ReplyMessageJpaEntity> findFirstByContentAndThreadId(@Param("content") String content, @Param("parentMessageId") Long parentMessageId);

    /**
     * Counts the number of replies for a given message ID.
     *
     * @param messageId the ID of the parent message
     * @return the number of replies for the given message ID
     */
    @Query("SELECT COUNT(m) FROM ReplyMessageJpaEntity m WHERE m.parentMessageId = :messageId")
    int countRepliesByMessageId(@Param("messageId") Long messageId);

    /**
     * Finds reply messages by user ID within the specified date range.
     *
     * @param userId     the ID of the user
     * @param startDate  the start date of the date range
     * @param endDate    the end date of the date range
     * @return a list of reply messages created by the user within the specified date range
     */
    @Query("SELECT m FROM ReplyMessageJpaEntity m WHERE m.userId = :userId AND m.createdAt BETWEEN :startDate AND :endDate")
    List<ReplyMessageJpaEntity> findRepliesMessageByUserIdAndDateRange(@Param("userId") String userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}