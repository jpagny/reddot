package com.elysium.reddot.ms.topic.infrastructure.configuration;

import com.elysium.reddot.ms.topic.infrastructure.constant.RabbitMQConstant;
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

        // ### TOPIC / BOARD

        // Create topic / board exchange
        TopicExchange topicBoardExchange = new TopicExchange(RabbitMQConstant.EXCHANGE_TOPIC_BOARD);
        rabbitAdmin.declareExchange(topicBoardExchange);

        // Create queue
        Queue topicExistsQueue = new Queue(RabbitMQConstant.QUEUE_TOPIC_EXIST);
        rabbitAdmin.declareQueue(topicExistsQueue);

        // Create binding between queue and exchange for requests
        Binding topicRequestBinding = BindingBuilder.bind(topicExistsQueue).to(topicBoardExchange).with(RabbitMQConstant.REQUEST_TOPIC_EXIST);
        rabbitAdmin.declareBinding(topicRequestBinding);

        return rabbitAdmin;
    }
}
