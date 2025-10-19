package com.muhammetakduman.order_inventory.mapper;

import com.muhammetakduman.order_inventory.dto.order.OrderResponse;
import com.muhammetakduman.order_inventory.model.order.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderResponse toResponse(Order o) {
        return new OrderResponse(
                o.getId(),
                o.getStatus().name(),
                o.getCreatedAt(),
                o.getItems().stream()
                        .map(i -> new OrderResponse.OrderLine(
                                i.getProduct().getId(),
                                i.getQuantity()
                        ))
                        .toList()
        );
    }
}
