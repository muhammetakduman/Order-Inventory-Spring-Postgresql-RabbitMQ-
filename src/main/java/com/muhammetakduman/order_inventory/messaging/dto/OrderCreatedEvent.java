package com.muhammetakduman.order_inventory.messaging.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class OrderCreatedEvent {
    private UUID orderId;
    private List<OrderItem> items;

    public OrderCreatedEvent() { }

    public OrderCreatedEvent(UUID orderId, List<OrderItem> items) {
        this.orderId = orderId;
        this.items = items;
    }

    @Setter
    @Getter
    public static class OrderItem {
        private UUID productId;
        private int quantity;

        public OrderItem() { }

        public OrderItem(UUID productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

    }
}
