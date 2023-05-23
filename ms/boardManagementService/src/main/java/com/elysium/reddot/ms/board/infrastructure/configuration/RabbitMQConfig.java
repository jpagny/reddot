package com.elysium.reddot.ms.board.infrastructure.configuration;

import com.elysium.reddot.ms.board.infrastructure.constant.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

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


        // ### BOARD / THREAD

        // Create topic exchange
        TopicExchange boardThreadExchange = new TopicExchange(RabbitMQConstant.EXCHANGE_BOARD_THREAD);
        rabbitAdmin.declareExchange(boardThreadExchange);

        // Create queue
        Queue boardExistsQueue = new Queue(RabbitMQConstant.QUEUE_BOARD_EXIST);
        rabbitAdmin.declareQueue(boardExistsQueue);

        // Create binding between queue and exchange for requests
        Binding boardRequestBinding = BindingBuilder.bind(boardExistsQueue).to(boardThreadExchange).with(RabbitMQConstant.REQUEST_BOARD_EXIST);
        rabbitAdmin.declareBinding(boardRequestBinding);

        return rabbitAdmin;

    }
}
