package com.elysium.reddot.ms.topic.domain.model;

import java.util.Objects;

/**
 * This class represents a model for a topic.
 */
public class TopicModel {
    private final Long id;
    private final String name;
    private String label;
    private String description;

    /**
     * Constructs a new TopicModel with the given id, name, label, and description.
     *
     * @param id          The unique identifier for this topic.
     * @param name        The name of this topic.
     * @param label       The label of this topic.
     * @param description A description of this topic.
     */
    public TopicModel(Long id, String name, String label, String description) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopicModel topicModel = (TopicModel) o;

        return Objects.equals(id, topicModel.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}