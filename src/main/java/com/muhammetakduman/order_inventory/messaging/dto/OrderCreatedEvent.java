package com.muhammetakduman.order_inventory.messaging.dto;

import java.util.List;
import java.util.UUID;

public class OrderCreatedEvent {
    private UUID orderId;
    private List<OrderItem> items;

    public OrderCreatedEvent() { }

    public OrderCreatedEvent(UUID orderId, List<OrderItem> items) {
        this.orderId = orderId;
        this.items = items;
    }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public static class OrderItem {
        private UUID productId;
        private int quantity;

        public OrderItem() { }

        public OrderItem(UUID productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public UUID getProductId() { return productId; }
        public void setProductId(UUID productId) { this.productId = productId; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}
