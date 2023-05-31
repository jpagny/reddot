package com.elysium.reddot.ms.user.infrastructure.constant;

public final class RabbitMQConstant {

    private RabbitMQConstant() {
    }

    public static final String EXCHANGE_STATISTIC_USER = "statistic_user.exchange";
    public static final String QUEUE_FETCH_ALL_USERS = "fetch.all_users.queue";
    public static final String REQUEST_FETCH_ALL_USERS = "fetch.all_users.request";


    public static final String EXCHANGE_STATISTIC_MESSAGE = "statistic_message.exchange";
    public static final String QUEUE_COUNT_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE = "count_messages_by_user_in_range_dates.queue";
    public static final String REQUEST_COUNT_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE = "count_messages_by_user_in_range_dates.request";


    public static final String EXCHANGE_STATISTIC_REPLYMESSAGE = "statistic_replymessage.exchange";
    public static final String QUEUE_COUNT_REPLIES_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE = "count_replies_message_by_user_in_range_dates.queue";
    public static final String REQUEST_COUNT_REPLIES_MESSAGE_BY_USER_IN_RANGE_DATES_QUEUE = "count_replies_message_by_user_in_range_dates.request";

}
