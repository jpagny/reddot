package com.elysium.reddot.ms.statistic.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private LocalDateTime date;
    private Long countMessages;
    private String userId;

}
