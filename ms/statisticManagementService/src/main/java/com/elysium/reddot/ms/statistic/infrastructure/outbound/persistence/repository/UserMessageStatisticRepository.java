package com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.entity.UserMessageStatisticJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMessageStatisticRepository extends JpaRepository<UserMessageStatisticJpaEntity, Long> {
}
