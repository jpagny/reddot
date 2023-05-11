package com.elysium.reddot.ms.statistic.infrastructure.outbound.job;

import com.elysium.reddot.ms.statistic.application.service.StatisticApplicationServiceImpl;
import com.elysium.reddot.ms.statistic.infrastructure.outbound.rabbitMQ.requester.AllUsersIdRequester;
import com.elysium.reddot.ms.statistic.infrastructure.outbound.rabbitMQ.requester.CountMessagesByUserBetweenTwoDatesRequester;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class CalculateDailyUserMessagesStatisticJob extends QuartzJobBean {

    private final AllUsersIdRequester allUsersIdRequester;
    private final CountMessagesByUserBetweenTwoDatesRequester countMessagesByUserBetweenTwoDatesRequester;
    private final StatisticApplicationServiceImpl statisticApplicationService;

    @Override
    protected void executeInternal(@NotNull JobExecutionContext context) {
        List<String> allUsers = fetchAllUsers();
        processUsers(allUsers);
    }

    private List<String> fetchAllUsers() {
        return allUsersIdRequester.fetchAllUsers();
    }

    private void processUsers(List<String> users) {
        users.parallelStream().forEach(this::processByUser);
    }

    private void processByUser(String userId) {
        LocalDateTime[] period = getYesterdayPeriod();
        int messageCount = fetchMessageCountForUser(userId, period[0], period[1]);
        statisticApplicationService.calculateDailyUserMessages(messageCount, userId);
    }

    private LocalDateTime[] getYesterdayPeriod() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusDays(7).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = now.plusDays(1).withHour(0).withMinute(0).withSecond(0);
        return new LocalDateTime[]{start, end};
    }

    private int fetchMessageCountForUser(String userId, LocalDateTime start, LocalDateTime end) {
        return countMessagesByUserBetweenTwoDatesRequester.fetchCountMessageByUserBetweenTwoDate(userId, start, end);
    }


}
