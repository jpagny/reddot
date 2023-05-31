package com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.repository;

import com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.entity.UserMessageStatisticJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * A repository interface for managing UserMessageStatisticJpaEntity objects.
 **/
public interface UserMessageStatisticRepository extends JpaRepository<UserMessageStatisticJpaEntity, Long> {

    /**
     * Retrieves the count of messages by type, user ID, and date.
     *
     * @param type   the type of the statistic.
     * @param userId the user ID.
     * @param date   the date of the statistic.
     * @return the count of messages.
     */
    @Query("SELECT m.countMessages FROM UserMessageStatisticJpaEntity m WHERE m.userId = :userId AND DATE(m.date) = :date AND m.typeStatistic = :type")
    Integer getCountMessagesByTypeAndUserIdAndDate(
            @Param("type") String type,
            @Param("userId") String userId,
            @Param("date") java.sql.Date date
    );


}
