package com.elysium.reddot.ms.thread.infrastructure.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

        // ### BOARD / THREAD

        // Create topic exchange
        TopicExchange boardThreadExchange = new TopicExchange("boardThreadExchange");
        rabbitAdmin.declareExchange(boardThreadExchange);

        // Create queue
        Queue boardExistsQueue = new Queue("board.exists.queue");
        rabbitAdmin.declareQueue(boardExistsQueue);

        // Create binding between queue and exchange for requests
        Binding boardRequestBinding = BindingBuilder.bind(boardExistsQueue).to(boardThreadExchange).with("board.exists.request");
        rabbitAdmin.declareBinding(boardRequestBinding);

        return rabbitAdmin;

    }
}
