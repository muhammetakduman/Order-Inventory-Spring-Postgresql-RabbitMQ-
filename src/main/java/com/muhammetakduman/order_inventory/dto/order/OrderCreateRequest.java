package com.muhammetakduman.order_inventory.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderCreateRequest(
        @NotEmpty @Valid List<Item> items
) {
    public record Item(
            @NotNull UUID productId,
            @Min(1) int quantity
    ) {}
}
