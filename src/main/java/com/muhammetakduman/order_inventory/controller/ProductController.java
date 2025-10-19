package com.muhammetakduman.order_inventory.controller;


import com.muhammetakduman.order_inventory.model.product.Product;
import com.muhammetakduman.order_inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    
    @GetMapping
    public ResponseEntity<List<Product>> getAll(){
        return ResponseEntity.ok(productService.getAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getById(id));
    }
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product){
        return ResponseEntity.ok(productService.create(product));
    }
    @PostMapping("/{id}/decrease")
    public ResponseEntity<Void> decreaseStock(
            @PathVariable UUID id,
            @RequestParam int quantity
    ) {
        productService.decreaseStock(id, quantity);
        return ResponseEntity.noContent().build();
    }

}
