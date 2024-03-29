package com.elysium.reddot.ms.topic.infrastructure.constant;

/**
 * This class holds constant values that are used for RabbitMQ communication.
 * These values are related to exchanges, queues, and requests in RabbitMQ.
 */
public final class RabbitMQConstant {

    private RabbitMQConstant() {
    }

    // ### EXCHANGES
    public static final String EXCHANGE_TOPIC_BOARD = "topic_board.exchange";


    // ### QUEUES
    public static final String QUEUE_TOPIC_EXIST = "topic_exists.queue";


    // ### REQUESTS
    public static final String REQUEST_TOPIC_EXIST = "topic_exists.request";


}
