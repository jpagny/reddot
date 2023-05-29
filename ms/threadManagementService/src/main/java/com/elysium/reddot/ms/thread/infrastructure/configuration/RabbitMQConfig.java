package com.elysium.reddot.ms.thread.infrastructure.configuration;

import com.elysium.reddot.ms.thread.infrastructure.constant.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The RabbitMQConfig class is a configuration class for RabbitMQ integration.
 * It provides a bean for RabbitAdmin to declare exchanges, queues, and bindings.
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

        // ### BOARD / THREAD

        // Create board / thread exchange
        TopicExchange boardThreadExchange = new TopicExchange(RabbitMQConstant.EXCHANGE_BOARD_THREAD);
        rabbitAdmin.declareExchange(boardThreadExchange);

        // Create queue
        Queue boardExistsQueue = new Queue(RabbitMQConstant.QUEUE_BOARD_EXIST);
        rabbitAdmin.declareQueue(boardExistsQueue);

        // Create binding between queue and exchange for requests
        Binding boardRequestBinding = BindingBuilder.bind(boardExistsQueue).to(boardThreadExchange).with(RabbitMQConstant.REQUEST_BOARD_EXIST);
        rabbitAdmin.declareBinding(boardRequestBinding);


        // ### THREAD / MESSAGE

        // Create thread / message exchange
        TopicExchange threadMessageExchange = new TopicExchange(RabbitMQConstant.EXCHANGE_THREAD_MESSAGE);
        rabbitAdmin.declareExchange(threadMessageExchange);

        // Create queue
        Queue threadExistsQueue = new Queue(RabbitMQConstant.QUEUE_THREAD_EXIST);
        rabbitAdmin.declareQueue(threadExistsQueue);

        // Create binding between queue and exchange for requests
        Binding threadRequestBinding = BindingBuilder.bind(threadExistsQueue).to(threadMessageExchange).with(RabbitMQConstant.REQUEST_THREAD_EXIST);
        rabbitAdmin.declareBinding(threadRequestBinding);

        return rabbitAdmin;

    }
}
