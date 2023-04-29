package com.elysium.reddot.ms.user.infrastructure.inbound.rest.route;

public enum UserRouteConstants {

    CREATE_TOPIC("direct:createUser");

    private final String routeName;

    UserRouteConstants(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
