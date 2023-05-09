package com.elysium.reddot.ms.message.domain.model;

import java.util.Objects;

public class MessageModel {

    private final Long id;
    private String content;
    private Long parentMessageID ;
    private Long threadId;
    private String userId;

    public MessageModel(Long id, String label, Long threadId, String userId,Long parentMessageID) {
        this.id = id;
        this.content = label;
        this.threadId = threadId;
        this.userId = userId;
        this.parentMessageID = parentMessageID;
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

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
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
                ", parentMessageID='" + parentMessageID + '\'' +
                '}';
    }



}
