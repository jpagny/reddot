package com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Entity class representing a topic in the database.
 */
@Entity
@Table(name = "topics")
@Data
public class TopicJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String label;

    private String description;

}
