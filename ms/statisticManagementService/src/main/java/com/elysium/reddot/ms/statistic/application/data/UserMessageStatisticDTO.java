package com.elysium.reddot.ms.statistic.application.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessageStatisticDTO {
    private Long id;
    private LocalDateTime date;
    private Long countMessages;
    private String userId;
}
