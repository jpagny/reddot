package com.elysium.reddot.ms.statistic.domain.port.inbound;


import java.time.LocalDate;

/**
 * This interface represents a service for managing user message statistics.
 */
public interface StatisticManagementService {

    /**
     * Inserts the daily count of user messages into the database.
     *
     * @param userId the ID of the user.
     * @param countMessages the number of messages sent by the user.
     */
    void insertCountDailyUserMessages(String userId, Integer countMessages);

    /**
     * Inserts the daily count of user reply messages into the database.
     *
     * @param userId the ID of the user.
     * @param countRepliesMessage the number of reply messages sent by the user.
     */
    void insertCountDailyUserRepliesMessage(String userId, Integer countRepliesMessage);

    /**
     * Inserts the daily count of total user messages (messages + replies) into the database.
     *
     * @param userId the ID of the user.
     * @param countTotalMessages the total number of messages (messages + replies) sent by the user.
     */
    void insertCountDailyUserTotalMessages(String userId, Integer countTotalMessages);

    /**
     * Gets the count of messages of a specific type sent by a user on a particular date.
     *
     * @param type the type of the message (e.g., message, reply).
     * @param userId the ID of the user.
     * @param date the date when the messages were sent.
     * @return the count of messages of the specified type sent by the user on the given date.
     */
    Integer getMessageCountByTypeAndByUserIdFromDate(String type, String userId, LocalDate date);

}
