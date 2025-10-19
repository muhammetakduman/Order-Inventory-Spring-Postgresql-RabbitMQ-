package com.muhammetakduman.order_inventory.service;


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
    @Transactional
    public void decreaseStock(UUID id, int quantitiy){
        Product product = getById(id);
        if (product.getStock() < quantitiy)
            throw new IllegalStateException("Not enough stock");
        product.setStock(product.getStock() - quantitiy);
        productRepository.save(product);
    }
}
