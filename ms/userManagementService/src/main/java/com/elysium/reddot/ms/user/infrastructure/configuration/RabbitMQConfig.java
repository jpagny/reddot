package com.elysium.reddot.ms.user.infrastructure.configuration;

import com.elysium.reddot.ms.user.infrastructure.constant.RabbitMQConstant;
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

        // ### build exchange between statisticManagementService module and userManagementService module

        TopicExchange statisticUserExchange = new TopicExchange(RabbitMQConstant.EXCHANGE_STATISTIC_USER);
        rabbitAdmin.declareExchange(statisticUserExchange);

        Queue listAllUsersQueue = new Queue(RabbitMQConstant.QUEUE_FETCH_ALL_USERS);
        rabbitAdmin.declareQueue(listAllUsersQueue);

        Binding listUserRequestBinding = BindingBuilder
                .bind(listAllUsersQueue)
                .to(statisticUserExchange)
                .with(RabbitMQConstant.REQUEST_FETCH_ALL_USERS);
        rabbitAdmin.declareBinding(listUserRequestBinding);

        return rabbitAdmin;
    }

}