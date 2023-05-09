package com.elysium.reddot.ms.statistic.infrastructure.persistence.repository;

import com.elysium.reddot.ms.statistic.infrastructure.persistence.entity.UserMessageStatisticJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMessageStatisticRepository extends JpaRepository<UserMessageStatisticJpaEntity, Long> {
}
