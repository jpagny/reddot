package com.elysium.reddot.ms.replymessage.infrastructure.constant;

/**
 * Constants class for RabbitMQ configurations.
 * This class provides constant values for exchanges, queues, and request bindings used in RabbitMQ.
 */
public final class RabbitMQConstant {

    private RabbitMQConstant() {
    }

    // ### EXCHANGES
    public static final String EXCHANGE_MESSAGE_REPLYMESSAGE = "message_replymessage.exchange";
    public static final String EXCHANGE_STATISTIC_REPLYMESSAGE = "statistic_replymessage.exchange";


    // ### QUEUES
    public static final String QUEUE_MESSAGE_EXIST = "message_exists.queue";
    public static final String QUEUE_COUNT_REPLIES_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE = "count_replies_message_by_user_in_range_dates.queue";


    // ### REQUESTS
    public static final String REQUEST_MESSAGE_EXIST = "message_exists.request";
    public static final String REQUEST_COUNT_REPLIES_MESSAGE_BY_USER_IN_RANGE_DATES_QUEUE = "count_replies_message_by_user_in_range_dates.request";


}
