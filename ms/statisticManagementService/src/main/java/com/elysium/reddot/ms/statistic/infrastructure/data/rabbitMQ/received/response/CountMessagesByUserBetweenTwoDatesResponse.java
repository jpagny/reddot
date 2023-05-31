package com.elysium.reddot.ms.statistic.infrastructure.data.rabbitMQ.received.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents the response containing the total count of messages sent by a user between two dates.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CountMessagesByUserBetweenTwoDatesResponse {
    private Integer countMessagesTotal;
}