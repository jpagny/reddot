package com.elysium.reddot.ms.message.infrastructure.data.rabbitmq.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CountMessageByUserBetweenTwoDatesResponse {
    private Integer countMessagesTotal;
}
