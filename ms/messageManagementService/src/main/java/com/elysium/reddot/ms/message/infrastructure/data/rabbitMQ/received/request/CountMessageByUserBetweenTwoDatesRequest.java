package com.elysium.reddot.ms.message.infrastructure.data.rabbitmq.received.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A data class that represents a request to count messages
 * sent by a specific user between two dates.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountMessageByUserBetweenTwoDatesRequest {
    private String userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime onStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime onEnd;
}
