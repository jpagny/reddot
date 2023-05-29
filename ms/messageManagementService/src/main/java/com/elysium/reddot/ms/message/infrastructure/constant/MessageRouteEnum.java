package com.elysium.reddot.ms.message.infrastructure.constant;

/**
 * Enum for Message routes.
 * This enum provides a mapping between message actions and their corresponding routes.
 */
public enum MessageRouteEnum {

    GET_ALL_MESSAGES("direct:getAllMessages"),
    GET_MESSAGE_BY_ID("direct:getMessageById"),
    CREATE_MESSAGE("direct:createMessage"),
    UPDATE_MESSAGE("direct:updateMessage");

    private final String routeName;

    MessageRouteEnum(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
