package com.elysium.reddot.ms.replymessage.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a reply message in the application.
 */
public class ReplyMessageModel {

    private Long id;
    private String content;
    private Long parentMessageID;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Constructor creating a full reply message object.
     *
     * @param id the unique identifier of the message
     * @param content the text content of the message
     * @param parentMessageID the identifier of the message this is a reply to
     * @param userId the identifier of the user who created the message
     * @param createdAt the time when the message was created
     * @param updatedAt the time when the message was last updated
     */
    public ReplyMessageModel(Long id, String content, Long parentMessageID, String userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.parentMessageID = parentMessageID;
        this.userId = userId;
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
    }

    /**
     * Constructor creating a reply message object without an id or timestamps.
     *
     * @param content the text content of the message
     * @param parentMessageID the identifier of the message this is a reply to
     * @param userId the identifier of the user who created the message
     */
    public ReplyMessageModel(String content, Long parentMessageID, String userId) {
        this.content = content;
        this.parentMessageID = parentMessageID;
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
