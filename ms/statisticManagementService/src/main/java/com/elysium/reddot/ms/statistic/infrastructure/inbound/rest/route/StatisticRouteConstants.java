package com.elysium.reddot.ms.statistic.infrastructure.inbound.rest.route;

public enum StatisticRouteConstants {

    GET_MESSAGE_COUNT_BY_TYPE_AND_BY_USER_FROM_DATE("direct:getMessageCountByTypeAndByUserFromDate");

    private final String routeName;

    StatisticRouteConstants(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
