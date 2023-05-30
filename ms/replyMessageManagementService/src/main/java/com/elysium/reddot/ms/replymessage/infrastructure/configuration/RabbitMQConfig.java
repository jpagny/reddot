package com.elysium.reddot.ms.replymessage.infrastructure.configuration;

import com.elysium.reddot.ms.replymessage.infrastructure.constant.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for RabbitMQ. Sets up exchanges, queues, and bindings for the application.
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

        // ### MESSAGE / REPLY_MESSAGE

        // Create thread / message exchange
        TopicExchange threadMessageExchange = new TopicExchange(RabbitMQConstant.EXCHANGE_MESSAGE_REPLYMESSAGE);
        rabbitAdmin.declareExchange(threadMessageExchange);

        // Create queue
        Queue messageExistsQueue = new Queue(RabbitMQConstant.QUEUE_MESSAGE_EXIST);
        rabbitAdmin.declareQueue(messageExistsQueue);

        // Create binding between queue and exchange for requests
        Binding messageRequestBinding = BindingBuilder.bind(messageExistsQueue).to(threadMessageExchange).with(RabbitMQConstant.REQUEST_MESSAGE_EXIST);
        rabbitAdmin.declareBinding(messageRequestBinding);


        // STATISTIC / REPLY_MESSAGE

        // Create message / reply_message exchange
        TopicExchange statisticReplyMessageMessageExchange = new TopicExchange(RabbitMQConstant.EXCHANGE_STATISTIC_MESSAGE);
        rabbitAdmin.declareExchange(statisticReplyMessageMessageExchange);

        // Create queue
        Queue countReplyMessageByUserRangeDate = new Queue(RabbitMQConstant.QUEUE_COUNT_REPLIES_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE);
        rabbitAdmin.declareQueue(countReplyMessageByUserRangeDate);

        // Create binding between queue and exchange for requests
        Binding statisticReplyRequestBinding = BindingBuilder.bind(countReplyMessageByUserRangeDate).to(statisticReplyMessageMessageExchange).with(RabbitMQConstant.REQUEST_COUNT_REPLIES_MESSAGE_BY_USER_IN_RANGE_DATES_QUEUE);
        rabbitAdmin.declareBinding(statisticReplyRequestBinding);

        return rabbitAdmin;
    }
}
