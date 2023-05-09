package com.elysium.reddot.ms.replymessage.infrastructure.configuration;

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

        // ### MESSAGE / REPLY_MESSAGE

        // Create thread / message exchange
        TopicExchange threadMessageExchange = new TopicExchange("messageReplyMessageExchange");
        rabbitAdmin.declareExchange(threadMessageExchange);

        // Create queue
        Queue messageExistsQueue = new Queue("message.exists.queue");
        rabbitAdmin.declareQueue(messageExistsQueue);

        // Create binding between queue and exchange for requests
        Binding messageRequestBinding = BindingBuilder.bind(messageExistsQueue).to(threadMessageExchange).with("message.exists.request");
        rabbitAdmin.declareBinding(messageRequestBinding);

        return rabbitAdmin;
    }
}
