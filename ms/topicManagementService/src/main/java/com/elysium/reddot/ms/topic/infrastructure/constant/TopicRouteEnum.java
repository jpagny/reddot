package com.elysium.reddot.ms.topic.infrastructure.constant;

/**
 * This enumeration defines the routes for various topic-related operations in the application.
 */
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
