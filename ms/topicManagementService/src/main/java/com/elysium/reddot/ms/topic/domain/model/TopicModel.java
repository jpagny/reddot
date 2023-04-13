package com.elysium.reddot.ms.topic.domain.model;

import java.util.Objects;

public class TopicModel implements Cloneable {
    private final Long id;
    private final String name;
    private String label;
    private String description;

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

    @Override
    public TopicModel clone() {
        try {
            return (TopicModel) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning failed for TopicModel", e);
        }
    }


}