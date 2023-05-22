package com.elysium.reddot.ms.message.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class MessageModel {

    private Long id;
    private String content;
    private Long threadId;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MessageModel(){}

    public MessageModel(Long id, String content, Long threadId, String userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.threadId = threadId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public MessageModel(String content, Long threadId, String userId){
        this.content = content;
        this.threadId = threadId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
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

        MessageModel messageModel = (MessageModel) o;

        return Objects.equals(id, messageModel.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", threadId='" + threadId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }



}
