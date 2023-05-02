package com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.route;

public enum AuthenticationRouteConstants {

    LOGIN("direct:authenticateUser"),
    LOGOUT("direct:logout");

    private final String routeName;

    AuthenticationRouteConstants(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
