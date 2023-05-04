package com.elysium.reddot.ms.board.domain.model;

import java.util.Objects;

public class BoardModel {
    private final Long id;
    private final String name;
    private String label;
    private String description;
    private Long topic_id;

    public BoardModel(Long id, String name, String label, String description, Long topicId) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.description = description;
        this.topic_id = topicId;
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

    public Long getTopicId(){
        return topic_id;
    }

    public void setTopicId(Long topicId){
        this.topic_id = topicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardModel boardModel = (BoardModel) o;

        return Objects.equals(id, boardModel.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}