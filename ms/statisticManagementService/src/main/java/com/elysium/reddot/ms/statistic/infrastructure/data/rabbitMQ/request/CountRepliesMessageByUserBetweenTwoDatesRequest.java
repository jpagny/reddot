package com.elysium.reddot.ms.statistic.infrastructure.data.rabbitMQ.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * A Data Transfer Object (DTO) class that represents a request containing
 * a user ID and a time interval (start and end times), with the purpose
 * of retrieving the total count of reply messages sent by the user in this time frame.
 **/
@Data
@AllArgsConstructor
public class CountRepliesMessageByUserBetweenTwoDatesRequest {
    private String userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime onStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime onEnd;
}
