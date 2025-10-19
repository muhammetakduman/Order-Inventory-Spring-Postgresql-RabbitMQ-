package com.muhammetakduman.order_inventory.dto.order;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String status,
        Instant createdAt,
        List<OrderLine> items
) {
    public record OrderLine(UUID productId, int quantity) {}
}
