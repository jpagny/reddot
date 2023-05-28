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

    @Query("SELECT m FROM ReplyMessageJpaEntity m WHERE m.content = :content AND m.parentMessageId = :parentMessageId ORDER BY m.createdAt")
    Optional<ReplyMessageJpaEntity> findFirstByContentAndThreadId(@Param("content") String content, @Param("parentMessageId") Long parentMessageId);

    @Query("SELECT COUNT(m) FROM ReplyMessageJpaEntity m WHERE m.parentMessageId = :messageId")
    int countRepliesByMessageId(@Param("messageId") Long messageId);

    @Query("SELECT m FROM ReplyMessageJpaEntity m WHERE m.userId = :userId AND m.createdAt BETWEEN :startDate AND :endDate")
    List<ReplyMessageJpaEntity> findRepliesMessageByUserIdAndDateRange(@Param("userId") String userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}