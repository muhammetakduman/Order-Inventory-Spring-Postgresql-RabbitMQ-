package com.muhammetakduman.order_inventory.messaging.consumer;

import com.muhammetakduman.order_inventory.messaging.dto.OrderCreatedEvent;
import com.muhammetakduman.order_inventory.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsListener {

    private final ProductService productService;

    public OrderEventsListener(ProductService productService) { this.productService = productService; }

    @RabbitListener(queues = "${app.rabbit.queue}")
    public void onOrderCreated(OrderCreatedEvent event) {
        for (OrderCreatedEvent.OrderItem item : event.getItems()) {
            productService.decreaseStock(item.getProductId(), item.getQuantity());
        }
    }
}
