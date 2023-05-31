package com.elysium.reddot.ms.statistic.infrastructure.data.rabbitMQ.received.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) class that represents a response containing
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CountRepliesMessageByUserBetweenTwoDatesResponse {
    private Integer countMessagesTotal;
}