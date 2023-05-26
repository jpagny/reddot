package com.elysium.reddot.ms.replymessage.infrastructure.constant;

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
