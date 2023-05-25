package com.elysium.reddot.ms.topic.infrastructure.constant;

public enum TopicRouteEnum {

    GET_ALL_TOPICS("direct:getAllTopics"),
    GET_TOPIC_BY_ID("direct:getTopicById"),
    CREATE_TOPIC("direct:createTopic"),
    UPDATE_TOPIC("direct:updateTopic"),
    DELETE_TOPIC("direct:deleteTopic");

    private final String routeName;

    TopicRouteEnum(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
