package com.elysium.reddot.ms.statistic.domain.port.inbound;

public interface StatisticManagementService {

    void calculateDailyUserMessages(Integer countMessage, String userId);

}
