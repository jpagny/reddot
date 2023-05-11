package com.elysium.reddot.ms.statistic.domain.port.inbound;


import java.time.LocalDate;

public interface StatisticManagementService {

    void insertCountDailyUserMessages(String userId, Integer countMessages);

    void insertCountDailyUserRepliesMessage(String userId, Integer countRepliesMessage);

    void insertCountDailyUserTotalMessages(String userId, Integer countTotalMessages);

    Integer getMessageCountByTypeAndByUserIdFromDate(String type, String userId, LocalDate date);

}
