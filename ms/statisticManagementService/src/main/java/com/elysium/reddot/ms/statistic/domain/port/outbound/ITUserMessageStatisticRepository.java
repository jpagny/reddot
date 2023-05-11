package com.elysium.reddot.ms.statistic.domain.port.outbound;

import com.elysium.reddot.ms.statistic.domain.model.UserMessageStatisticModel;

import java.time.LocalDate;

public interface ITUserMessageStatisticRepository {
    void createUserMessageStatistic(UserMessageStatisticModel userMessageStatisticModel);
    Integer getCountMessagesByTypeAndUserIdAndDate(String type, String userId, LocalDate date);
}
