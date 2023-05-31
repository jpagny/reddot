package com.elysium.reddot.ms.replymessage.infrastructure.data.rabbitmq.received.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Request object for counting the number of reply messages created by a user between two dates.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountRepliesMessageByUserBetweenTwoDatesRequest {
    private String userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime onStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime onEnd;
}
