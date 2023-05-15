package com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.route;

public enum AuthenticationRouteEnum {

    LOGIN("direct:authenticateUser"),
    LOGOUT("direct:logout");

    private final String routeName;

    AuthenticationRouteEnum(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
