package com.elysium.reddot.ms.statistic.application.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessageStatisticDTO {
    private Long id;
    private LocalDate date;
    private Long countMessages;
    private String userId;
    private String type;
}
