package com.elysium.reddot.ms.thread.infrastructure.constant;

/**
 * The ThreadRouteEnum enum represents the route names for thread-related operations.
 * It provides the route names for getting all threads, getting a thread by ID, creating a thread,
 * updating a thread.
 */
public enum ThreadRouteEnum {

    GET_ALL_THREADS("direct:getAllThreads"),
    GET_THREAD_BY_ID("direct:getThreadById"),
    CREATE_THREAD("direct:createThread"),
    UPDATE_THREAD("direct:updateThread");

    private final String routeName;

    ThreadRouteEnum(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
