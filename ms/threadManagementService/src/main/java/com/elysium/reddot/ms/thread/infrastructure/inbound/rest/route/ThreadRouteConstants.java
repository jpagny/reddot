package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.route;

public enum ThreadRouteConstants {

    GET_ALL_TOPICS("direct:getAllThreads"),
    GET_TOPIC_BY_ID("direct:getThreadById"),
    CREATE_TOPIC("direct:createThread"),
    UPDATE_TOPIC("direct:updateThread"),
    DELETE_TOPIC("direct:deleteThread");

    private final String routeName;

    ThreadRouteConstants(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
