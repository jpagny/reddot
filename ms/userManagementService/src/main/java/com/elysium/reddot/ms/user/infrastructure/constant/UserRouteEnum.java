package com.elysium.reddot.ms.user.infrastructure.constant;

public enum UserRouteEnum {

    USER_REGISTRATION("direct:userRegistration");

    private final String routeName;

    UserRouteEnum(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
