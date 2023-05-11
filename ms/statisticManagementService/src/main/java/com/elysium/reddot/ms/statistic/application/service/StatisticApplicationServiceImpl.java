package com.elysium.reddot.ms.statistic.application.service;

import com.elysium.reddot.ms.statistic.domain.model.UserMessageStatisticModel;
import com.elysium.reddot.ms.statistic.domain.port.inbound.StatisticManagementService;
import com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.UserMessageStatisticRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StatisticApplicationServiceImpl implements StatisticManagementService {

    private final UserMessageStatisticRepositoryAdapter userMessageStatisticRepository;

    private static String TYPE_COUNT_MESSAGES_BY_USER_DAILY = "TYPE_COUNT_MESSAGES_BY_USER_DAILY";
    private static String TYPE_COUNT_REPLIES_MESSAGE_BY_USER_DAILY = "TYPE_COUNT_REPLIES_MESSAGE_BY_USER_DAILY";
    private static String TYPE_COUNT_TOTAL_MESSAGES_BY_USER_DAILY = "TYPE_COUNT_TOTAL_MESSAGES_BY_USER_DAILY";


    @Override
    public void insertCountDailyUserMessages(String userId, Integer countMessage) {
        UserMessageStatisticModel userMessageStatisticModel = new UserMessageStatisticModel();
        userMessageStatisticModel.setCountMessages(countMessage);
        userMessageStatisticModel.setUserId(userId);
        userMessageStatisticModel.setDate(LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0));
        userMessageStatisticModel.setTypeStatistic(TYPE_COUNT_MESSAGES_BY_USER_DAILY);
        userMessageStatisticRepository.createUserMessageStatistic(userMessageStatisticModel);
    }

    @Override
    public void insertCountDailyUserRepliesMessage(String userId, Integer countMessage) {
        UserMessageStatisticModel userMessageStatisticModel = new UserMessageStatisticModel();
        userMessageStatisticModel.setCountMessages(countMessage);
        userMessageStatisticModel.setUserId(userId);
        userMessageStatisticModel.setDate(LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0));
        userMessageStatisticModel.setTypeStatistic(TYPE_COUNT_REPLIES_MESSAGE_BY_USER_DAILY);
        userMessageStatisticRepository.createUserMessageStatistic(userMessageStatisticModel);
    }

    @Override
    public void insertCountDailyUserTotalMessages(String userId, Integer countMessage) {
        UserMessageStatisticModel userMessageStatisticModel = new UserMessageStatisticModel();
        userMessageStatisticModel.setCountMessages(countMessage);
        userMessageStatisticModel.setUserId(userId);
        userMessageStatisticModel.setDate(LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0));
        userMessageStatisticModel.setTypeStatistic(TYPE_COUNT_TOTAL_MESSAGES_BY_USER_DAILY);
        userMessageStatisticRepository.createUserMessageStatistic(userMessageStatisticModel);
    }

    @Override
    public UserMessageStatisticModel getMessageCountByTypeAndByUserIdFromDate(String type, String userId, LocalDate date) {


        return null;
    }


}
