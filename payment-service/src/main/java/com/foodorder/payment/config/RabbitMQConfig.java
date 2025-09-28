package com.foodorder.payment.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    
    // Exchange names
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    
    // Queue names
    public static final String PAYMENT_QUEUE = "payment.queue";
    public static final String ORDER_STATUS_QUEUE = "order.status.queue";
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    
    // Routing keys
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";
    public static final String PAYMENT_PROCESSED_ROUTING_KEY = "payment.processed";
    
    // Exchanges
    @Bean
    TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }
    
    @Bean
    TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }
    
    // Queues
    @Bean
    Queue paymentQueue() {
        return QueueBuilder.durable(PAYMENT_QUEUE).build();
    }
    
    @Bean
    Queue orderStatusQueue() {
        return QueueBuilder.durable(ORDER_STATUS_QUEUE).build();
    }
    
    @Bean
    Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE).build();
    }
    
    // Bindings
    @Bean
    Binding paymentBinding() {
        return BindingBuilder
            .bind(paymentQueue())
            .to(orderExchange())
            .with(ORDER_CREATED_ROUTING_KEY);
    }
    
    @Bean
    Binding orderStatusBinding() {
        return BindingBuilder
            .bind(orderStatusQueue())
            .to(paymentExchange())
            .with(PAYMENT_PROCESSED_ROUTING_KEY);
    }
    
    @Bean
    Binding notificationBinding() {
        return BindingBuilder
            .bind(notificationQueue())
            .to(paymentExchange())
            .with(PAYMENT_PROCESSED_ROUTING_KEY);
    }
    
    // Message converter
    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    // RabbitTemplate configuration
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
    
    // Listener container factory
    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }
}