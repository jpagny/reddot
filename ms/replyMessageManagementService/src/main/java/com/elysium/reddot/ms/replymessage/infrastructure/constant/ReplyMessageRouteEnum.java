package com.elysium.reddot.ms.replymessage.infrastructure.constant;

/**
 * Enumeration for Reply Message routes.
 * Each route represents a direct exchange routing key used for RabbitMQ message routing.
 */
public enum ReplyMessageRouteEnum {

    GET_ALL_REPLIES_MESSAGE("direct:getAllRepliesMessage"),
    GET_REPLY_MESSAGE_BY_ID("direct:getReplyMessageById"),
    CREATE_REPLY_MESSAGE("direct:createReplyMessage"),
    UPDATE_REPLY_MESSAGE("direct:updateReplyMessage");

    private final String routeName;

    ReplyMessageRouteEnum(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
