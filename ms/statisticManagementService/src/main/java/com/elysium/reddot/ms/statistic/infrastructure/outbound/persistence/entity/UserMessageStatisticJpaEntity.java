package com.elysium.reddot.ms.statistic.infrastructure.outbound.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_message_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMessageStatisticJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "DATE")
    private LocalDate date;
    private Integer countMessages;
    private String userId;
    private String typeStatistic;

}
