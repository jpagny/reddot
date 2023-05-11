package com.elysium.reddot.ms.statistic.application.service;

import com.elysium.reddot.ms.statistic.domain.model.UserMessageStatisticModel;
import com.elysium.reddot.ms.statistic.domain.port.inbound.StatisticManagementService;
import com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.UserMessageStatisticRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticApplicationServiceImpl implements StatisticManagementService {

    private final UserMessageStatisticRepositoryAdapter userMessageStatisticRepository;

    private static String TYPE_COUNT_MESSAGES_BY_USER_DAILY = "TYPE_COUNT_MESSAGES_BY_USER_DAILY";
    private static String TYPE_COUNT_REPLIES_MESSAGE_BY_USER_DAILY = "TYPE_COUNT_REPLIES_MESSAGE_BY_USER_DAILY";
    private static String TYPE_COUNT_TOTAL_MESSAGES_BY_USER_DAILY = "TYPE_COUNT_TOTAL_MESSAGES_BY_USER_DAILY";


    @Override
    public void insertCountDailyUserMessages(String userId, Integer countMessage) {
        log.debug("Before Recheck Userid 1 " + userId);
        UserMessageStatisticModel userMessageStatisticModel = new UserMessageStatisticModel();
        userMessageStatisticModel.setCountMessages(countMessage);
        userMessageStatisticModel.setUserId(userId);
        userMessageStatisticModel.setDate(LocalDate.now().minusDays(1));
        userMessageStatisticModel.setTypeStatistic(TYPE_COUNT_MESSAGES_BY_USER_DAILY);
        log.debug("Recheck Userid 1 " + userMessageStatisticModel.getUserId());
        userMessageStatisticRepository.createUserMessageStatistic(userMessageStatisticModel);
    }

    @Override
    public void insertCountDailyUserRepliesMessage(String userId, Integer countMessage) {
        log.debug("Before Recheck Userid 2 " + userId);

        UserMessageStatisticModel userMessageStatisticModel = new UserMessageStatisticModel();
        userMessageStatisticModel.setCountMessages(countMessage);
        userMessageStatisticModel.setUserId(userId);
        userMessageStatisticModel.setDate(LocalDate.now());
        userMessageStatisticModel.setTypeStatistic(TYPE_COUNT_REPLIES_MESSAGE_BY_USER_DAILY);
        log.debug("Recheck Userid 2 " + userMessageStatisticModel.toString());
        userMessageStatisticRepository.createUserMessageStatistic(userMessageStatisticModel);
    }

    @Override
    public void insertCountDailyUserTotalMessages(String userId, Integer countMessage) {
        log.debug("Before Recheck Userid 3 " + userId);

        UserMessageStatisticModel userMessageStatisticModel = new UserMessageStatisticModel();
        userMessageStatisticModel.setCountMessages(countMessage);
        userMessageStatisticModel.setUserId(userId);
        userMessageStatisticModel.setDate(LocalDate.now().minusDays(1));
        userMessageStatisticModel.setTypeStatistic(TYPE_COUNT_TOTAL_MESSAGES_BY_USER_DAILY);
        log.debug("Recheck Userid 3 " + userMessageStatisticModel.toString());
        userMessageStatisticRepository.createUserMessageStatistic(userMessageStatisticModel);
    }

    @Override
    public Integer getMessageCountByTypeAndByUserIdFromDate(String type, String userId, LocalDate date) {
         return userMessageStatisticRepository.getCountMessagesByTypeAndUserIdAndDate(type,userId,date);
    }


}
