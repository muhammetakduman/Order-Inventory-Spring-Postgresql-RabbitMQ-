package com.muhammetakduman.order_inventory.service;


import com.muhammetakduman.order_inventory.NotEnoughStockException;
import com.muhammetakduman.order_inventory.model.product.Product;
import com.muhammetakduman.order_inventory.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> getAll(){
        return productRepository.findAll();
    }
    @Transactional
    public Product create(Product product){
        productRepository.findByNameIgnoreCase(product.getName())
                .ifPresent(product1 -> {throw new IllegalArgumentException("Product Name Already exists");});
        return productRepository.save(product);
    }
    @Transactional(readOnly = true)
    public Product getById(UUID id){
        return productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Product not found"));
    }
    // ProductService.java
    @Transactional
    public Product decreaseStock(UUID id, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));

        if (product.getStock() < quantity) {
            throw new NotEnoughStockException("Not enough stock for product " + id);
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
        return product;
    }

}
