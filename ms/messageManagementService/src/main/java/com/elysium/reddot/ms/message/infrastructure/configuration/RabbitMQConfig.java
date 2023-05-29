package com.elysium.reddot.ms.message.infrastructure.configuration;

import com.elysium.reddot.ms.message.infrastructure.constant.RabbitMQConstant;
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


        // MESSAGE / REPLY_MESSAGE

        // Create message / reply_message exchange
        TopicExchange threadMessageMessageReplyExchange = new TopicExchange(RabbitMQConstant.EXCHANGE_MESSAGE_REPLYMESSAGE);
        rabbitAdmin.declareExchange(threadMessageMessageReplyExchange);

        // Create queue
        Queue messageExistsQueue = new Queue(RabbitMQConstant.QUEUE_MESSAGE_EXIST);
        rabbitAdmin.declareQueue(messageExistsQueue);

        // Create binding between queue and exchange for requests
        Binding messageRequestBinding = BindingBuilder.bind(messageExistsQueue).to(threadMessageMessageReplyExchange).with(RabbitMQConstant.REQUEST_MESSAGE_EXIST);
        rabbitAdmin.declareBinding(messageRequestBinding);


        // STATISTIC / MESSAGE

        // Create message / reply_message exchange
        TopicExchange statisticMessageMessageExchange = new TopicExchange(RabbitMQConstant.EXCHANGE_STATISTIC_MESSAGE);
        rabbitAdmin.declareExchange(statisticMessageMessageExchange);

        // Create queue
        Queue countMessageByUserRangeDate = new Queue(RabbitMQConstant.QUEUE_COUNT_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE);
        rabbitAdmin.declareQueue(countMessageByUserRangeDate);

        // Create binding between queue and exchange for requests
        Binding statisticRequestBinding = BindingBuilder.bind(countMessageByUserRangeDate).to(statisticMessageMessageExchange).with(RabbitMQConstant.REQUEST_COUNT_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE);
        rabbitAdmin.declareBinding(statisticRequestBinding);


        return rabbitAdmin;
    }
}
