package com.elysium.reddot.ms.message.infrastructure.data.rabbitmq.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A data class that represents the response for counting the total number of messages
 * by a user between two dates.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CountMessageByUserBetweenTwoDatesResponse {
    private Integer countMessagesTotal;
}
