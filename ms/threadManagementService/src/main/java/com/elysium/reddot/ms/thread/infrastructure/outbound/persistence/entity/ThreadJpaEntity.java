package com.elysium.reddot.ms.thread.infrastructure.outbound.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * The ThreadJpaEntity class represents a JPA entity for the "threads" table in the database.
 * It maps the columns of the table to the fields of the entity class.
 */
@Entity
@Table(name = "threads")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThreadJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String label;

    private String description;

    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "user_id")
    private String userId;
}
