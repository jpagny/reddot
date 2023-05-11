package com.elysium.reddot.ms.statistic.domain.port.inbound;

import com.elysium.reddot.ms.statistic.domain.model.UserMessageStatisticModel;

import java.time.LocalDate;

public interface StatisticManagementService {

    void insertCountDailyUserMessages(String userId, Integer countMessages);

    void insertCountDailyUserRepliesMessage(String userId, Integer countRepliesMessage);

    void insertCountDailyUserTotalMessages(String userId, Integer countTotalMessages);

    UserMessageStatisticModel getMessageCountByTypeAndByUserIdFromDate(String type, String userId, LocalDate date);

}
