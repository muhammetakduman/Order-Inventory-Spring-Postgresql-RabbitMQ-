package com.muhammetakduman.order_inventory.controller;


import com.muhammetakduman.order_inventory.dto.order.OrderCreateRequest;
import com.muhammetakduman.order_inventory.dto.order.OrderResponse;
import com.muhammetakduman.order_inventory.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderCreateRequest request){
        return ResponseEntity.ok(orderService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> get(@PathVariable UUID id){
        return ResponseEntity.ok(orderService.get(id));
    }
}
