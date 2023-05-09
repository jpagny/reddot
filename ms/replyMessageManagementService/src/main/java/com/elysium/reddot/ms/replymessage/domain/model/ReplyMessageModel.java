package com.elysium.reddot.ms.replymessage.domain.model;

import java.util.Objects;

public class ReplyMessageModel {

    private final Long id;
    private String content;
    private Long parentMessageID;
    private String userId;

    public ReplyMessageModel(Long id, String label, Long parentMessageID, String userId) {
        this.id = id;
        this.content = label;
        this.parentMessageID = parentMessageID;
        this.userId = userId;
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
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", userId='" + userId + '\'' +
                ", parentMessageID='" + parentMessageID + '\'' +
                '}';
    }


}
