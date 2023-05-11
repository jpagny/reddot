package com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.entity.ReplyMessageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyMessageJpaRepository extends JpaRepository<ReplyMessageJpaEntity, Long> {
    @Query("SELECT COUNT(m) FROM ReplyMessageJpaEntity m WHERE m.parentMessageId = :messageId")
    int countRepliesByMessageId(@Param("messageId") Long messageId);
}