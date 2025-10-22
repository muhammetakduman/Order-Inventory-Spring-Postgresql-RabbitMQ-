package com.muhammetakduman.order_inventory.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${app.rabbit.exchange}")
    private String exchangeName;

    @Value("${app.rabit.queue}")
    private String queueName;

    @Value("${app.rabbit.routing-key}")
    private String routingKey;

    /// pattern redirection
    @Bean
    public TopicExchange orderEventsExchange(){
        return ExchangeBuilder.topicExchange(exchangeName)
                .durable(true)
                .build();
    }

    /// select queue
    @Bean
    public Queue orderCreatedQueue(){
        return QueueBuilder.durable(queueName).build();
    }

    /// routerkey equals exchange
    @Bean
    public Binding orderCreatedBinding(Queue orderCreatedQueue, TopicExchange orderEventsExchange){
        return BindingBuilder.bind(orderCreatedQueue).to(orderEventsExchange).with(routingKey);
    }

    /// json converter
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /// add converter for producer template
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter());
        return template;
    }
}
