package com.elysium.reddot.ms.replymessage.infrastructure.data.rabbitMQ.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CountRepliesMessageByUserBetweenTwoDatesResponse {
    private Integer countMessagesTotal;
}