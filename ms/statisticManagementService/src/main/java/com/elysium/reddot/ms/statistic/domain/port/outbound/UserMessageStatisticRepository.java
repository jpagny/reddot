package com.elysium.reddot.ms.statistic.domain.port.outbound;

import com.elysium.reddot.ms.statistic.domain.model.UserMessageStatisticModel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This interface represents a repository for managing UserMessageStatisticModel.
 */
public interface UserMessageStatisticRepository {

    /**
     * Creates a new user message statistic record in the database.
     *
     * @param userMessageStatisticModel the user message statistic to be created.
     */
    void createUserMessageStatistic(UserMessageStatisticModel userMessageStatisticModel);

    /**
     * Retrieves the count of messages of a certain type, sent by a specific user on a particular date.
     *
     * @param type the type of the message (e.g., message, reply).
     * @param userId the ID of the user.
     * @param date the date when the messages were sent.
     * @return the count of messages of the specified type sent by the user on the given date.
     */
    Integer getCountMessagesByTypeAndUserIdAndDate(String type, String userId, LocalDate date);
}
