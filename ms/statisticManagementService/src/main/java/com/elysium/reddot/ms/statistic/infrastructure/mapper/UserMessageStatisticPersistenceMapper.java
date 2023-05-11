package com.elysium.reddot.ms.statistic.infrastructure.mapper;

import com.elysium.reddot.ms.statistic.domain.model.UserMessageStatisticModel;
import com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.entity.UserMessageStatisticJpaEntity;

public class UserMessageStatisticPersistenceMapper {

    private UserMessageStatisticPersistenceMapper() {
    }

    public static UserMessageStatisticJpaEntity toEntity(UserMessageStatisticModel userMessageStatisticModel) {
        UserMessageStatisticJpaEntity userMessageStatisticJpaEntity = new UserMessageStatisticJpaEntity();
        userMessageStatisticJpaEntity.setId(userMessageStatisticModel.getId());
        userMessageStatisticJpaEntity.setDate(userMessageStatisticModel.getDate());
        userMessageStatisticJpaEntity.setUserId(userMessageStatisticModel.getUserId());
        userMessageStatisticJpaEntity.setCountMessages(userMessageStatisticModel.getCountMessages());
        userMessageStatisticJpaEntity.setTypeStatistic(userMessageStatisticModel.getTypeStatistic());
        return userMessageStatisticJpaEntity;
    }

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
