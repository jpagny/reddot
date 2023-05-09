package com.elysium.reddot.ms.message.infrastructure.configuration;

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

        // ### ThREAD / MESSAGE

        // Create thread / message exchange
        TopicExchange threadMessageExchange = new TopicExchange("threadMessageExchange");
        rabbitAdmin.declareExchange(threadMessageExchange);

        // Create queue
        Queue threadExistsQueue = new Queue("thread.exists.queue");
        rabbitAdmin.declareQueue(threadExistsQueue);

        // Create binding between queue and exchange for requests
        Binding threadRequestBinding = BindingBuilder.bind(threadExistsQueue).to(threadMessageExchange).with("thread.exists.request");
        rabbitAdmin.declareBinding(threadRequestBinding);

        return rabbitAdmin;
    }
}
