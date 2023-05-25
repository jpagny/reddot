package com.elysium.reddot.ms.thread.infrastructure.constant;

public final class RabbitMQConstant {

    private RabbitMQConstant(){}

    // ### EXCHANGES
    public static final String EXCHANGE_BOARD_THREAD = "board_thread.exchange";
    public static final String EXCHANGE_THREAD_MESSAGE = "thread_message.exchange";



    // ### QUEUES
    public static final String QUEUE_BOARD_EXIST = "board_exists.queue";
    public static final String QUEUE_THREAD_EXIST = "thread_exists.queue";

    // ### REQUESTS
    public static final String REQUEST_BOARD_EXIST = "board_exists.request";
    public static final String REQUEST_THREAD_EXIST = "thread_exists.request";


}
