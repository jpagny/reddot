package com.elysium.reddot.ms.statistic.infrastructure.data.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CountMessageByUserBetweenTwoDatesResponse {
    private static final long serialVersionUID = 1L;
    private Integer countMessages;
    private LocalDateTime onStart;
    private LocalDateTime onEnd;
}
