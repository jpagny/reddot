package com.elysium.reddot.ms.statistic.infrastructure.outbound.job;

import com.elysium.reddot.ms.statistic.application.service.StatisticApplicationServiceImpl;
import com.elysium.reddot.ms.statistic.infrastructure.outbound.rabbitMQ.requester.AllUsersIdRequester;
import com.elysium.reddot.ms.statistic.infrastructure.outbound.rabbitMQ.requester.CountMessagesByUserBetweenTwoDatesRequester;
import com.elysium.reddot.ms.statistic.infrastructure.outbound.rabbitMQ.requester.CountRepliesMessageByUserBetweenTwoDatesRequester;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class CalculateDailyUserMessagesStatisticJob extends QuartzJobBean {

    private final AllUsersIdRequester allUsersIdRequester;
    private final CountMessagesByUserBetweenTwoDatesRequester countMessagesByUserBetweenTwoDatesRequester;
    private final CountRepliesMessageByUserBetweenTwoDatesRequester countRepliesMessageByUserBetweenTwoDatesResponse;

    private final StatisticApplicationServiceImpl statisticApplicationService;
    private ConcurrentHashMap<String, Integer> mapCountMessagesByUser;
    private ConcurrentHashMap<String, Integer> mapCountRepliesMessageByUser;


    @Override
    protected void executeInternal(@NotNull JobExecutionContext context) {
        mapCountMessagesByUser = new ConcurrentHashMap<>();
        mapCountRepliesMessageByUser = new ConcurrentHashMap<>();
        List<String> allUsers = fetchAllUsers();
        processUsers(allUsers);
    }

    private List<String> fetchAllUsers() {
        return allUsersIdRequester.fetchAllUsers();
    }

    private void processUsers(List<String> users) {
        List<CompletableFuture<Void>> messageFutures = users.stream()
                .map(user -> CompletableFuture.runAsync(() -> processCalculateDailyUserMessages(user)))
                .collect(Collectors.toList());

        List<CompletableFuture<Void>> replyFutures = users.stream()
                .map(user -> CompletableFuture.runAsync(() -> processCalculateDailyUserRepliesMessage(user)))
                .collect(Collectors.toList());

        CompletableFuture<Void> allMessageFutures = CompletableFuture.allOf(messageFutures.toArray(new CompletableFuture[0]));
        CompletableFuture<Void> allReplyFutures = CompletableFuture.allOf(replyFutures.toArray(new CompletableFuture[0]));

        CompletableFuture.allOf(allMessageFutures, allReplyFutures).join();

        for (String userId : users) {
            Integer countMessages = mapCountMessagesByUser.get(userId);
            Integer countRepliesMessage = mapCountRepliesMessageByUser.get(userId);
            insertIntoDatabase(userId, countMessages, countRepliesMessage);
        }

    }

    private void processCalculateDailyUserMessages(String userId) {
        LocalDateTime[] period = getYesterdayPeriod();
        int messageCount = fetchMessagesCountForUser(userId, period[0], period[1]);
        mapCountMessagesByUser.put(userId, messageCount);
    }

    private void processCalculateDailyUserRepliesMessage(String userId) {
        LocalDateTime[] period = getYesterdayPeriod();
        int messageCount = fetchRepliesMessageCountForUser(userId, period[0], period[1]);
        mapCountRepliesMessageByUser.put(userId, messageCount);
    }

    private LocalDateTime[] getYesterdayPeriod() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusDays(7).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = now.plusDays(1).withHour(0).withMinute(0).withSecond(0);
        return new LocalDateTime[]{start, end};
    }

    private int fetchMessagesCountForUser(String userId, LocalDateTime start, LocalDateTime end) {
        return countMessagesByUserBetweenTwoDatesRequester.fetchCountMessageByUserBetweenTwoDate(userId, start, end);
    }

    private int fetchRepliesMessageCountForUser(String userId, LocalDateTime start, LocalDateTime end) {
        return countRepliesMessageByUserBetweenTwoDatesResponse.fetchCountRepliesMessageByUserBetweenTwoDate(userId, start, end);
    }

    private void insertIntoDatabase(String userId, Integer countMessages, Integer countRepliesMessage) {
        log.debug("CHeck userId : " + userId);
        Integer totalMessages = countMessages + countRepliesMessage;
        statisticApplicationService.insertCountDailyUserMessages(userId, countMessages);
        statisticApplicationService.insertCountDailyUserRepliesMessage(userId, countRepliesMessage);
        statisticApplicationService.insertCountDailyUserTotalMessages(userId, totalMessages);
    }


}
