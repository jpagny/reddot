package com.elysium.reddot.ms.replymessage.infrastructure.data.rabbitmq.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for the response containing the count of reply messages created by a user between two dates.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CountRepliesMessageByUserBetweenTwoDatesResponse {
    private Integer countMessagesTotal;
}
