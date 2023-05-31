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

/**
 * A component that calculates daily user message statistics using the Quartz scheduling library.
 **/
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


    /**
     * Executes the job. This is called by the Quartz scheduler.
     *
     * @param context the execution context provided by Quartz.
     */
    @Override
    protected void executeInternal(@NotNull JobExecutionContext context) {
        log.debug("Executing job to calculate daily user message statistics.");

        mapCountMessagesByUser = new ConcurrentHashMap<>();
        mapCountRepliesMessageByUser = new ConcurrentHashMap<>();

        List<String> allUsers = fetchAllUsers();
        log.debug("Fetched {} users.", allUsers.size());

        if (!allUsers.isEmpty()) {
            processUsers(allUsers);
        }

        log.debug("Finished executing job to calculate daily user message statistics.");
    }

    private List<String> fetchAllUsers() {
        log.info("Fetching all users.");
        return allUsersIdRequester.fetchAllUsers();
    }

    private void processUsers(List<String> users) {

        log.debug("Executing job to calculate daily user message statistics.");

        CompletableFuture<Void> allMessageFutures = CompletableFuture.allOf(users.stream()
                .map(user -> CompletableFuture.runAsync(() -> processCalculateDailyUserMessages(user))).toArray(CompletableFuture[]::new));

        CompletableFuture<Void> allReplyFutures = CompletableFuture.allOf(users.stream()
                .map(user -> CompletableFuture.runAsync(() -> processCalculateDailyUserRepliesMessage(user))).toArray(CompletableFuture[]::new));

        CompletableFuture.allOf(allMessageFutures, allReplyFutures).join();

        log.debug("Processing results and inserting into the database.");

        for (String userId : users) {
            Integer countMessages = mapCountMessagesByUser.get(userId);
            Integer countRepliesMessage = mapCountRepliesMessageByUser.get(userId);
            int totalMessage = countMessages + countRepliesMessage;

            log.debug("Processing user {} - countMessages: {}, countRepliesMessage: {}, total: {}", userId, countMessages, countRepliesMessage, totalMessage);

            if (countMessages < 0 || countRepliesMessage < 0) {
                log.debug("There is a problem with the connection between microservices. Unable to fetch information.");

            } else {

                if (totalMessage > 0) {
                    insertIntoDatabase(userId, countMessages, countRepliesMessage);

                } else {
                    log.debug("This user {} has not sent any messages / replies message between this period.", userId);
                }
            }

        }

        log.debug("Finished processing users.");
    }

    private void processCalculateDailyUserMessages(String userId) {
        log.debug("Processing daily user messages for user: {}", userId);

        LocalDateTime[] period = getYesterdayPeriod();
        int messageCount = fetchMessagesCountForUser(userId, period[0], period[1]);

        log.debug("User: {} - Message count: {}", userId, messageCount);

        mapCountMessagesByUser.put(userId, messageCount);

        log.debug("Finished processing daily user messages for user: {}", userId);
    }

    private void processCalculateDailyUserRepliesMessage(String userId) {
        log.debug("Processing daily user replies messages for user: {}", userId);

        LocalDateTime[] period = getYesterdayPeriod();
        int messageCount = fetchRepliesMessageCountForUser(userId, period[0], period[1]);

        log.debug("User: {} - Replies message count: {}", userId, messageCount);

        mapCountRepliesMessageByUser.put(userId, messageCount);

        log.debug("Finished processing daily user replies messages for user: {}", userId);
    }


    private LocalDateTime[] getYesterdayPeriod() {
        log.debug("Calculating the start and end LocalDateTime objects for the previous day's time period.");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = now.withHour(0).withMinute(0).withSecond(0);

        log.debug("Yesterday's period: Start: {}, End: {}", start, end);

        return new LocalDateTime[]{start, end};
    }

    private int fetchMessagesCountForUser(String userId, LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching messages count for user: {}, start: {}, end: {}", userId, start, end);

        int messageCount = countMessagesByUserBetweenTwoDatesRequester.fetchCountMessageByUserBetweenTwoDate(userId, start, end);

        log.debug("Fetched messages count: {} for user: {}, start: {}, end: {}", messageCount, userId, start, end);

        return messageCount;
    }

    private int fetchRepliesMessageCountForUser(String userId, LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching replies message count for user: {}, start: {}, end: {}", userId, start, end);

        int messageCount = countRepliesMessageByUserBetweenTwoDatesResponse.fetchCountRepliesMessageByUserBetweenTwoDate(userId, start, end);

        log.debug("Fetched replies message count: {} for user: {}, start: {}, end: {}", messageCount, userId, start, end);

        return messageCount;
    }

    private void insertIntoDatabase(String userId, Integer countMessages, Integer countRepliesMessage) {
        log.debug("Inserting daily user messages and replies count into the database for user: {}", userId);

        Integer totalMessages = countMessages + countRepliesMessage;
        statisticApplicationService.insertCountDailyUserMessages(userId, countMessages);
        statisticApplicationService.insertCountDailyUserRepliesMessage(userId, countRepliesMessage);
        statisticApplicationService.insertCountDailyUserTotalMessages(userId, totalMessages);

        log.debug("Inserted daily user messages and replies count into the database for user: {}", userId);
    }


}
