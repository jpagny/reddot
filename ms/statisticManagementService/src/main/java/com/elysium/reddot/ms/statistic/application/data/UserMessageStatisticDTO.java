package com.elysium.reddot.ms.statistic.application.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This class represents the Data Transfer Object (DTO) for UserMessageStatistic.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessageStatisticDTO {

    /**
     * The ID of the user message statistics.
     */
    private Long id;

    /**
     * The date the statistics were collected.
     */
    private LocalDate date;

    /**
     * The count of messages sent by a user on a particular date.
     */
    private Long countMessages;

    /**
     * The ID of the user.
     */
    private String userId;

    /**
     * The type of statistics represented by this DTO.
     */
    private String type;
}
