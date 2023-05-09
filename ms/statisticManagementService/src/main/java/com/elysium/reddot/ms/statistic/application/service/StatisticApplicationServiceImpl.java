package com.elysium.reddot.ms.statistic.application.service;

import com.elysium.reddot.ms.statistic.domain.model.UserMessageStatisticModel;
import com.elysium.reddot.ms.statistic.domain.port.inbound.StatisticManagementService;
import com.elysium.reddot.ms.statistic.infrastructure.persistence.UserMessageStatisticRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticApplicationServiceImpl implements StatisticManagementService {

    private final UserMessageStatisticRepositoryAdapter userMessageStatisticRepository;

    @Override
    public void calculateDailyUserMessages(Integer countMessage, String userId) {
        UserMessageStatisticModel userMessageStatisticModel = new UserMessageStatisticModel();
        userMessageStatisticRepository.createUserMessageStatistic(userMessageStatisticModel);
    }
}
