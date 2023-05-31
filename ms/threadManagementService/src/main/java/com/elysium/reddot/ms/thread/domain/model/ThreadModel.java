package com.elysium.reddot.ms.thread.domain.model;

import java.util.Objects;

/**
 * ThreadModel represents a discussion thread in a message board system.
 * Each thread is linked to a board by its boardId and created by a user represented by userId.
 */
public class ThreadModel {
    private final Long id;
    private final String name;
    private String label;
    private String description;
    private Long boardId;
    private String userId;

    /**
     * Constructs a new ThreadModel with the given parameters.
     *
     * @param id the unique identifier for the thread.
     * @param name the name of the thread.
     * @param label the label assigned to the thread.
     * @param description the description of the thread.
     * @param boardId the ID of the board the thread belongs to.
     * @param userId the ID of the user who created the thread.
     */
    public ThreadModel(Long id, String name, String label, String description, Long boardId, String userId) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.description = description;
        this.boardId = boardId;
        this.userId = userId;
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

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThreadModel threadModel = (ThreadModel) o;

        return Objects.equals(id, threadModel.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Thread{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", boardId='" + boardId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }


}