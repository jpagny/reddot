package com.elysium.reddot.ms.user.infrastructure.constant;

public final class RabbitMQConstant {

    private RabbitMQConstant(){}

    public static final String EXCHANGE_STATISTIC_USER = "statistic_user.exchange";
    public static final String QUEUE_FETCH_ALL_USERS = "fetch.all_users.queue";
    public static final String REQUEST_FETCH_ALL_USERS = "fetch.all_users.request";
}
