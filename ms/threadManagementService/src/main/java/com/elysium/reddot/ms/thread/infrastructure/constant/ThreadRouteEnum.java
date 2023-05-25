package com.elysium.reddot.ms.thread.infrastructure.constant;

public enum ThreadRouteEnum {

    GET_ALL_THREADS("direct:getAllThreads"),
    GET_THREAD_BY_ID("direct:getThreadById"),
    CREATE_THREAD("direct:createThread"),
    UPDATE_THREAD("direct:updateThread"),
    DELETE_THREAD("direct:deleteThread");

    private final String routeName;

    ThreadRouteEnum(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
