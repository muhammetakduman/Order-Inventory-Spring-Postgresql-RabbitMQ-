package com.muhammetakduman.order_inventory.messaging.producer;

import com.muhammetakduman.order_inventory.messaging.dto.OrderCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbit.exchange}")
    private String exchange;
    @Value("${app.rabbit.routing-key}")
    private String routingKey;

    public OrderEventsProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    /// event exception exchange

    public void publishOrderCreated(OrderCreatedEvent orderCreatedEvent){
        rabbitTemplate.convertAndSend(exchange,routingKey,orderCreatedEvent);
    }
}
