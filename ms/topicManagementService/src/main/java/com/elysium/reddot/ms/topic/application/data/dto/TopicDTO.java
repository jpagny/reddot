package com.elysium.reddot.ms.topic.application.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDTO {
    private Long id;
    private String name;
    private String label;
    private String description;
}