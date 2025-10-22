package com.muhammetakduman.order_inventory.controller;


import com.muhammetakduman.order_inventory.dto.order.ApiResponse;
import com.muhammetakduman.order_inventory.dto.order.OrderCreateRequest;
import com.muhammetakduman.order_inventory.dto.order.OrderResponse;
import com.muhammetakduman.order_inventory.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // OrderController.java
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(@Valid @RequestBody OrderCreateRequest req) {
        OrderResponse out = orderService.create(req);
        return ResponseEntity.ok(ApiResponse.ok(out));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> get(@PathVariable UUID id) {
        OrderResponse out = orderService.get(id);
        return ResponseEntity.ok(ApiResponse.ok(out));
    }
    @GetMapping("/allOrder") ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrder(){
        List<OrderResponse> orders = orderService.getAllOrder();
        return ResponseEntity.ok(ApiResponse.ok(orders));
    }

}
