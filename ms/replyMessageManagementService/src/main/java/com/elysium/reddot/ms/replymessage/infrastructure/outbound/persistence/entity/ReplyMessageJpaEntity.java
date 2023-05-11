package com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "repliesMessage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyMessageJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Long parentMessageId;

    private String userId;
}