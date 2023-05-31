package com.elysium.reddot.ms.statistic.infrastructure.mapper;

import com.elysium.reddot.ms.statistic.domain.model.UserMessageStatisticModel;
import com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.entity.UserMessageStatisticJpaEntity;

/**
 * A mapper class responsible for converting between UserMessageStatisticModel objects and UserMessageStatisticJpaEntity objects.
 **/
public class UserMessageStatisticPersistenceMapper {

    private UserMessageStatisticPersistenceMapper() {
    }

    /**
     * Converts a UserMessageStatisticModel to a UserMessageStatisticJpaEntity.
     *
     * @param userMessageStatisticModel the UserMessageStatisticModel to convert.
     * @return a new UserMessageStatisticJpaEntity with the same data as the input model.
     */
    public static UserMessageStatisticJpaEntity toEntity(UserMessageStatisticModel userMessageStatisticModel) {
        UserMessageStatisticJpaEntity userMessageStatisticJpaEntity = new UserMessageStatisticJpaEntity();
        userMessageStatisticJpaEntity.setId(userMessageStatisticModel.getId());
        userMessageStatisticJpaEntity.setDate(userMessageStatisticModel.getDate());
        userMessageStatisticJpaEntity.setUserId(userMessageStatisticModel.getUserId());
        userMessageStatisticJpaEntity.setCountMessages(userMessageStatisticModel.getCountMessages());
        userMessageStatisticJpaEntity.setTypeStatistic(userMessageStatisticModel.getTypeStatistic());
        return userMessageStatisticJpaEntity;
    }

    /**
     * Converts a UserMessageStatisticJpaEntity to a UserMessageStatisticModel.
     *
     * @param userMessageStatisticJpaEntity the UserMessageStatisticJpaEntity to convert.
     * @return a new UserMessageStatisticModel with the same data as the input entity.
     */
    public static UserMessageStatisticModel toModel(UserMessageStatisticJpaEntity userMessageStatisticJpaEntity) {
        return new UserMessageStatisticModel(
                userMessageStatisticJpaEntity.getId(),
                userMessageStatisticJpaEntity.getDate(),
                userMessageStatisticJpaEntity.getCountMessages(),
                userMessageStatisticJpaEntity.getUserId(),
                userMessageStatisticJpaEntity.getTypeStatistic()
        );
    }

}
