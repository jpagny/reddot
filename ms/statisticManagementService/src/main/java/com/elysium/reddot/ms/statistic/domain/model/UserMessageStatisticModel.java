package com.elysium.reddot.ms.statistic.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserMessageStatisticModel {

    private Long id;
    private LocalDateTime date;
    private Integer countMessages;
    private String userId;
    private String typeStatistic;

    public UserMessageStatisticModel(){
    }
    public UserMessageStatisticModel(Long id, LocalDateTime date, Integer countMessages, String userId, String typeStatistic){
        this.id = id;
        this.date = date;
        this.countMessages = countMessages;
        this.userId = userId;
        this.typeStatistic = typeStatistic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getCountMessages() {
        return countMessages;
    }

    public void setCountMessages(Integer countMessages) {
        this.countMessages = countMessages;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getTypeStatistic() {
        return typeStatistic;
    }

    public void setTypeStatistic(String typeStatistic) {
        this.typeStatistic = typeStatistic;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserMessageStatisticModel threadModel = (UserMessageStatisticModel) o;

        return Objects.equals(id, threadModel.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserMessageStatisticModel{" +
                "id=" + id +
                '}';
    }


}
