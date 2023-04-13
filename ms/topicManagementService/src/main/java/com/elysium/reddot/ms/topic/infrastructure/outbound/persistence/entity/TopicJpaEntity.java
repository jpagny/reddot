package com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "topics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topic_seq_generator")
    @SequenceGenerator(name = "topic_seq_generator", sequenceName = "topic_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String label;

    private String description;

}
