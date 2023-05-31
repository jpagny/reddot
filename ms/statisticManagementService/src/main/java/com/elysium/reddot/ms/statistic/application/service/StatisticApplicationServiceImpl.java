package com.elysium.reddot.ms.statistic.application.service;

import com.elysium.reddot.ms.statistic.application.constant.TypeCountEnum;
import com.elysium.reddot.ms.statistic.domain.model.UserMessageStatisticModel;
import com.elysium.reddot.ms.statistic.domain.port.inbound.StatisticManagementService;
import com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.UserMessageStatisticRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This class represents the implementation of the StatisticManagementService.
 * It is responsible for managing the operations related to the user message statistics.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticApplicationServiceImpl implements StatisticManagementService {

    private final UserMessageStatisticRepositoryAdapter userMessageStatisticRepository;


    /**
     * {@inheritDoc}
     */
    @Override
    public void insertCountDailyUserMessages(String userId, Integer countMessage) {
        log.info("Inserting daily user messages count for user id: {}", userId);

        UserMessageStatisticModel userMessageStatisticModel = new UserMessageStatisticModel(
                LocalDate.now().minusDays(1),
                countMessage,
                userId,
                TypeCountEnum.TYPE_COUNT_MESSAGES_BY_USER_DAILY.name()
        );

        userMessageStatisticRepository.createUserMessageStatistic(userMessageStatisticModel);

        log.info("Inserted daily user messages count for user id: {}", userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertCountDailyUserRepliesMessage(String userId, Integer countMessage) {
        log.info("Inserting daily user reply messages count for user id: {}", userId);

        UserMessageStatisticModel userMessageStatisticModel = new UserMessageStatisticModel(
                LocalDate.now().minusDays(1),
                countMessage,
                userId,
                TypeCountEnum.TYPE_COUNT_REPLIES_MESSAGE_BY_USER_DAILY.name()
        );

        userMessageStatisticRepository.createUserMessageStatistic(userMessageStatisticModel);

        log.info("Inserted daily user reply messages count for user id: {}", userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertCountDailyUserTotalMessages(String userId, Integer countMessage) {
        log.info("Inserting daily total messages count for user id: {}", userId);

        UserMessageStatisticModel userMessageStatisticModel = new UserMessageStatisticModel(
                LocalDate.now().minusDays(1),
                countMessage,
                userId,
                TypeCountEnum.TYPE_COUNT_TOTAL_MESSAGES_BY_USER_DAILY.name()
        );

        userMessageStatisticRepository.createUserMessageStatistic(userMessageStatisticModel);

        log.info("Inserted daily total messages count for user id: {}", userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getMessageCountByTypeAndByUserIdFromDate(String type, String userId, LocalDate date) {
        log.info("Retrieving message count by type: {}, user id: {}, and date: {}", type, userId, date);

        Integer count = userMessageStatisticRepository.getCountMessagesByTypeAndUserIdAndDate(type, userId, date);

        log.info("Retrieved message count: {}", count);

        return count;
    }


}
