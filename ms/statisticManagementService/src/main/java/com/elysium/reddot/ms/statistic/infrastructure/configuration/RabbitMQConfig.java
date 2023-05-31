package com.elysium.reddot.ms.statistic.infrastructure.configuration;

import com.elysium.reddot.ms.statistic.infrastructure.constant.RabbitMQConstant;
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

        // ### STATISTIC / USER

        TopicExchange statisticUserExchange = new TopicExchange(RabbitMQConstant.EXCHANGE_STATISTIC_USER);
        rabbitAdmin.declareExchange(statisticUserExchange);

        Queue listAllUsersQueue = new Queue(RabbitMQConstant.QUEUE_FETCH_ALL_USERS);
        rabbitAdmin.declareQueue(listAllUsersQueue);

        Binding listUserRequestBinding = BindingBuilder.bind(listAllUsersQueue).to(statisticUserExchange).with(RabbitMQConstant.REQUEST_FETCH_ALL_USERS);
        rabbitAdmin.declareBinding(listUserRequestBinding);


        // ### STATISTIC / MESSAGE

        TopicExchange statisticMessageMessageExchange = new TopicExchange(RabbitMQConstant.EXCHANGE_STATISTIC_MESSAGE);
        rabbitAdmin.declareExchange(statisticMessageMessageExchange);

        Queue countMessageByUserRangeDate = new Queue(RabbitMQConstant.QUEUE_COUNT_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE);
        rabbitAdmin.declareQueue(countMessageByUserRangeDate);

        Binding statisticRequestBinding = BindingBuilder.bind(countMessageByUserRangeDate).to(statisticMessageMessageExchange).with(RabbitMQConstant.REQUEST_COUNT_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE);
        rabbitAdmin.declareBinding(statisticRequestBinding);


        // STATISTIC / REPLY_MESSAGE

        TopicExchange statisticReplyMessageMessageExchange = new TopicExchange(RabbitMQConstant.EXCHANGE_STATISTIC_REPLYMESSAGE);
        rabbitAdmin.declareExchange(statisticReplyMessageMessageExchange);

        Queue countReplyMessageByUserRangeDate = new Queue(RabbitMQConstant.QUEUE_COUNT_REPLIES_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE);
        rabbitAdmin.declareQueue(countReplyMessageByUserRangeDate);

        Binding statisticReplyRequestBinding = BindingBuilder.bind(countReplyMessageByUserRangeDate).to(statisticReplyMessageMessageExchange).with(RabbitMQConstant.REQUEST_COUNT_REPLIES_MESSAGE_BY_USER_IN_RANGE_DATES_QUEUE);
        rabbitAdmin.declareBinding(statisticReplyRequestBinding);

        return rabbitAdmin;
    }
}
