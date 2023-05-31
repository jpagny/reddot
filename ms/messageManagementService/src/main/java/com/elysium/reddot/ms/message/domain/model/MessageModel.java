package com.elysium.reddot.ms.message.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This class represents a model of a Message within the application.
 * <p>
 * Each MessageModel instance represents a unique message in a thread. It contains details about the
 * message content, associated thread id, user id and the timestamps of when the message was created
 * and last updated.
 */
public class MessageModel {

    private Long id;
    private String content;
    private Long threadId;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MessageModel(){}

    /**
     * Constructs a MessageModel with all fields.
     *
     * @param id the unique identifier of the message
     * @param content the content of the message
     * @param threadId the id of the thread that this message belongs to
     * @param userId the id of the user who created the message
     * @param createdAt the timestamp of when the message was created
     * @param updatedAt the timestamp of when the message was last updated
     */
    public MessageModel(Long id, String content, Long threadId, String userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.threadId = threadId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Constructs a MessageModel with content, threadId and userId.
     *
     * @param content the content of the message
     * @param threadId the id of the thread that this message belongs to
     * @param userId the id of the user who created the message
     */
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
