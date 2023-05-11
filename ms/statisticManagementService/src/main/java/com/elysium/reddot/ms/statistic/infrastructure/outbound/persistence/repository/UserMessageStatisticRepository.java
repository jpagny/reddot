package com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.entity.UserMessageStatisticJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface UserMessageStatisticRepository extends JpaRepository<UserMessageStatisticJpaEntity, Long> {

    @Query("SELECT m.countMessages FROM UserMessageStatisticJpaEntity m WHERE m.userId = :userId AND DATE(m.date) = :date AND m.typeStatistic = :type")
    Long getCountMessagesByTypeAndUserIdAndDate(
            @Param("type") String type,
            @Param("userId") String userId,
            @Param("date") LocalDate date
    );



}
