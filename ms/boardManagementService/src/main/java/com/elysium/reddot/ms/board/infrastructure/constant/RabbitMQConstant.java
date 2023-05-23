package com.elysium.reddot.ms.board.infrastructure.constant;

public final class RabbitMQConstant {

    private RabbitMQConstant(){}

    // ### EXCHANGES
    public static final String EXCHANGE_TOPIC_BOARD = "topic_board.exchange";
    public static final String EXCHANGE_BOARD_THREAD = "board_thread.exchange";



    // ### QUEUES
    public static final String QUEUE_TOPIC_EXIST = "topic_exists.queue";
    public static final String QUEUE_BOARD_EXIST = "board_exists.queue";


    // ### REQUESTS
    public static final String REQUEST_TOPIC_EXIST = "topic_exists.request";
    public static final String REQUEST_BOARD_EXIST = "board_exists.request";


}
