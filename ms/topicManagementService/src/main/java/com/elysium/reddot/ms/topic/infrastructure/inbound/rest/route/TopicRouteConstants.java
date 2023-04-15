package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.route;

public enum TopicRouteConstants {

    GET_ALL_TOPICS("direct:getAllTopics"),
    GET_TOPIC_BY_ID("direct:getTopicById"),
    CREATE_TOPIC("direct:createTopic"),
    UPDATE_TOPIC("direct:updateTopic"),
    DELETE_TOPIC("direct:deleteTopic");

    private final String routeName;

    TopicRouteConstants(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
