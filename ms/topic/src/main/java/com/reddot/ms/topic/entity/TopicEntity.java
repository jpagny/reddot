package com.reddot.ms.topic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "topic")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TopicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @NotBlank(message = "name is required")
    @Size(max = 100)
    @Column(name = "name")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

    @NotBlank(message = "label is required")
    @Size(max = 100)
    @Column(name = "label")
    private String label;

    @NotBlank(message = "description is required")
    @Size(max = 500)
    @Column(name = "description")
    private String description;

}
