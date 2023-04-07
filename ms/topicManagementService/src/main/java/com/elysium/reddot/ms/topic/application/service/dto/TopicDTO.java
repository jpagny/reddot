package com.elysium.reddot.ms.topic.application.service.dto;

public class TopicDTO {
    private Long id;
    private String name;
    private String label;
    private String description;

    public TopicDTO() {
    }

    public TopicDTO(Long id, String name, String label, String description) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}