package com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence;

import com.elysium.reddot.ms.statistic.domain.model.UserMessageStatisticModel;
import com.elysium.reddot.ms.statistic.domain.port.outbound.UserMessageStatisticRepository;
import com.elysium.reddot.ms.statistic.infrastructure.mapper.UserMessageStatisticPersistenceMapper;
import com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.entity.UserMessageStatisticJpaEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * An implementation of the UserMessageStatisticRepository that acts as an adapter to the UserMessageStatisticJpaEntity repository.
 **/
@Component
@RequiredArgsConstructor
@Slf4j
public class UserMessageStatisticRepositoryAdapter implements UserMessageStatisticRepository {

    private final com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.repository.UserMessageStatisticRepository userMessageStatisticRepository;

    /**
     * Creates a new user message statistic record in the database.
     *
     * @param userMessageStatisticModel the user message statistic model to be created.
     */
    @Override
    public void createUserMessageStatistic(UserMessageStatisticModel userMessageStatisticModel) {
        UserMessageStatisticJpaEntity userMessageStatisticJpaEntity = UserMessageStatisticPersistenceMapper.toEntity(userMessageStatisticModel);
        userMessageStatisticRepository.save(userMessageStatisticJpaEntity);
    }

    /**
     * Retrieves the count of messages by type, user ID, and date from the database.
     *
     * @param type   the type of the statistic.
     * @param userId the user ID.
     * @param date   the date of the statistic.
     * @return the count of messages.
     */
    @Override
    public Integer getCountMessagesByTypeAndUserIdAndDate(String type, String userId, LocalDate date) {
        Date sqlDate = Date.valueOf(String.valueOf(date));
        return userMessageStatisticRepository.getCountMessagesByTypeAndUserIdAndDate(type, userId, sqlDate);
    }


}
