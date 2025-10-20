package com.muhammetakduman.order_inventory.controller;


import com.muhammetakduman.order_inventory.dto.order.ApiResponse;
import com.muhammetakduman.order_inventory.model.product.Product;
import com.muhammetakduman.order_inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// ProductController.java
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> get(@PathVariable UUID id) {
        Product product = productService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok(product));
    }

    @PostMapping("/{id}/decrease")
    public ResponseEntity<ApiResponse<Product>> decreaseStock(
            @PathVariable UUID id,
            @RequestParam int quantity
    ) {
        Product updated = productService.decreaseStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> create(@RequestBody Product product) {
        Product created = productService.create(product);
        return ResponseEntity.ok(ApiResponse.ok(created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAll() {
        List<Product> products = productService.getAll();
        return ResponseEntity.ok(ApiResponse.ok(products));
    }
}

