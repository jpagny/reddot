package com.elysium.reddot.ms.message.infrastructure.constant;

/**
 * This class contains constants for RabbitMQ configuration.
 * These constants are used to define exchanges, queues, and request strings.
 * The class is final and has a private constructor to prevent instantiation.
 */
public final class RabbitMQConstant {

    private RabbitMQConstant() {
    }

    // ### EXCHANGES THREAD / MESSAGE
    public static final String EXCHANGE_THREAD_MESSAGE = "thread_message.exchange";
    public static final String QUEUE_THREAD_EXIST = "thread_exists.queue";
    public static final String REQUEST_THREAD_EXIST = "thread_exists.request";


    // ### EXCHANGES MESSAGE / REPLY MESSAGE
    public static final String EXCHANGE_MESSAGE_REPLYMESSAGE = "message_replymessage.exchange";
    public static final String QUEUE_MESSAGE_EXIST = "message_exists.queue";
    public static final String REQUEST_MESSAGE_EXIST = "message_exists.request";


    // ### EXCHANGES STATISTIC / MESSAGE
    public static final String EXCHANGE_STATISTIC_MESSAGE = "statistic_message.exchange";
    public static final String QUEUE_COUNT_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE = "count_messages_by_user_in_range_dates.queue";
    public static final String REQUEST_COUNT_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE = "count_messages_by_user_in_range_dates.request";


}
