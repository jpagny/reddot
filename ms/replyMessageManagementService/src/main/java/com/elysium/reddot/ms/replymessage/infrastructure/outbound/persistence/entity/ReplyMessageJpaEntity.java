package com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a message entity in the database.
 */
@Entity
@Table(name = "replies_message")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyMessageJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @Column(name = "parent_message_id")
    private Long parentMessageId;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
