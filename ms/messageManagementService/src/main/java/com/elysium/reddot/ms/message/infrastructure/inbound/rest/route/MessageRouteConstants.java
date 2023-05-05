package com.elysium.reddot.ms.message.infrastructure.inbound.rest.route;

public enum MessageRouteConstants {

    GET_ALL_TOPICS("direct:getAllMessages"),
    GET_TOPIC_BY_ID("direct:getMessageById"),
    CREATE_TOPIC("direct:createMessage"),
    UPDATE_TOPIC("direct:updateMessage");

    private final String routeName;

    MessageRouteConstants(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
