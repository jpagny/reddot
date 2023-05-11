package com.elysium.reddot.ms.statistic.domain.port.inbound;

public interface StatisticManagementService {

    void insertCountDailyUserMessages(String userId, Integer countMessages);

    void insertCountDailyUserRepliesMessage(String userId, Integer countRepliesMessage);

    void insertCountDailyUserTotalMessages(String userId, Integer countTotalMessages);

}
