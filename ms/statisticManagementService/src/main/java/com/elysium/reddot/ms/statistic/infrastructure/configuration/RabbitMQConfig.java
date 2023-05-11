package com.elysium.reddot.ms.statistic.infrastructure.configuration;

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

        // STATISTIC / USER

        TopicExchange statisticUserExchange = new TopicExchange("statisticUserExchange");
        rabbitAdmin.declareExchange(statisticUserExchange);

        Queue listAllUsersQueue = new Queue("user.all.queue");
        rabbitAdmin.declareQueue(listAllUsersQueue);

        Binding listUserRequestBinding = BindingBuilder.bind(listAllUsersQueue).to(statisticUserExchange).with("user.all.request");
        rabbitAdmin.declareBinding(listUserRequestBinding);


        // STATISTIC / MESSAGE

        // Create message / reply_message exchange
        TopicExchange statisticMessageMessageExchange = new TopicExchange("statisticMessageExchange");
        rabbitAdmin.declareExchange(statisticMessageMessageExchange);

        // Create queue
        Queue countMessageByUserRangeDate = new Queue("count.message.user.dates.queue");
        rabbitAdmin.declareQueue(countMessageByUserRangeDate);

        // Create binding between queue and exchange for requests
        Binding statisticRequestBinding = BindingBuilder.bind(countMessageByUserRangeDate).to(statisticMessageMessageExchange).with("count.message.user.dates.request");
        rabbitAdmin.declareBinding(statisticRequestBinding);

        return rabbitAdmin;
    }
}
