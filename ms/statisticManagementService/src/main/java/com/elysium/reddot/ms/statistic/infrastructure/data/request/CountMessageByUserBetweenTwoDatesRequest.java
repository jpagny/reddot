package com.elysium.reddot.ms.statistic.infrastructure.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CountMessageByUserBetweenTwoDatesRequest {
    private String userId;
    private LocalDateTime onStart;
    private LocalDateTime onEnd;
}
