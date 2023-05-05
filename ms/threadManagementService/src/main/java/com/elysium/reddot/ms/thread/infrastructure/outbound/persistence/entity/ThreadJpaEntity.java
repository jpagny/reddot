package com.elysium.reddot.ms.thread.infrastructure.outbound.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private Long boardId;

    private String userId;
}
