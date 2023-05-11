package com.elysium.reddot.ms.replymessage.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class ReplyMessageModel {

    private final Long id;
    private String content;
    private Long parentMessageID;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public ReplyMessageModel(Long id, String label, Long parentMessageID, String userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = label;
        this.parentMessageID = parentMessageID;
        this.userId = userId;
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
    }

    public Long getId() {
        return id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getParentMessageID() {
        return parentMessageID;
    }

    public void setMessageId(Long parentMessageID) {
        this.parentMessageID = parentMessageID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReplyMessageModel messageModel = (ReplyMessageModel) o;

        return Objects.equals(id, messageModel.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ReplyMessage{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", userId='" + userId + '\'' +
                ", parentMessageID='" + parentMessageID + '\'' +
                '}';
    }



}
