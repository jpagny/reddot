package com.reddot.ms.topic.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TopicDTO {

    Long id;

    @NotNull
    String name;

    @NotNull
    String label;

    @NotNull
    String description;
}
