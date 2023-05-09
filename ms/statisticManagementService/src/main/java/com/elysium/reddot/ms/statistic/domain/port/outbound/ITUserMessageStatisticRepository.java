package com.elysium.reddot.ms.statistic.domain.port.outbound;

import com.elysium.reddot.ms.statistic.domain.model.UserMessageStatisticModel;

public interface ITUserMessageStatisticRepository {
    void createUserMessageStatistic(UserMessageStatisticModel userMessageStatisticModel);
}
