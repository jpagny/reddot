package com.elysium.reddot.ms.message.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.message.infrastructure.outbound.persistence.entity.MessageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageJpaRepository extends JpaRepository<MessageJpaEntity, Long> {
    @Query("SELECT COUNT(m) FROM MessageJpaEntity m WHERE m.parentMessageId = :messageId")
    int countRepliesByMessageId(@Param("messageId") Long messageId);
}