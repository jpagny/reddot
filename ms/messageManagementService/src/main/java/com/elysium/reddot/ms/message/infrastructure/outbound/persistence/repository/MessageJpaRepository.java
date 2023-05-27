package com.elysium.reddot.ms.message.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.message.infrastructure.outbound.persistence.entity.MessageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageJpaRepository extends JpaRepository<MessageJpaEntity, Long> {

    @Query("SELECT m FROM MessageJpaEntity m WHERE m.content = :content AND m.threadId = :threadId ORDER BY m.createdAt")
    Optional<MessageJpaEntity> findFirstByContentAndThreadId(@Param("content") String content, @Param("threadId") Long threadId);
    @Query("SELECT m FROM MessageJpaEntity m WHERE m.userId = :userId AND m.createdAt BETWEEN :startDate AND :endDate")
    List<MessageJpaEntity> findMessagesByUserIdAndDateRange(@Param("userId") String userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}