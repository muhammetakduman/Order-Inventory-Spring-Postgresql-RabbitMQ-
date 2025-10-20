package com.muhammetakduman.order_inventory;

import com.muhammetakduman.order_inventory.NotEnoughStockException;
import com.muhammetakduman.order_inventory.model.product.Product;
import com.muhammetakduman.order_inventory.repository.ProductRepository;
import com.muhammetakduman.order_inventory.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setup() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void decreaseStock_shouldUpdateAndReturnProduct() {
        UUID id = UUID.randomUUID();
        Product p = new Product();
        p.setId(id);
        p.setName("Laptop");
        p.setStock(10);
        p.setCreatedAt(Instant.now());
        p.setUpdatedAt(Instant.now());

        when(productRepository.findById(id)).thenReturn(Optional.of(p));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product updated = productService.decreaseStock(id, 3);

        assertEquals(7, updated.getStock());

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product saved = captor.getValue();
        assertEquals(7, saved.getStock());
    }

    @Test
    void decreaseStock_shouldThrowWhenProductMissing() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> productService.decreaseStock(id, 1));
    }

    @Test
    void decreaseStock_shouldThrowWhenQuantityInvalid() {
        UUID id = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> productService.decreaseStock(id, 0));
        assertThrows(IllegalArgumentException.class, () -> productService.decreaseStock(id, -2));
    }

    @Test
    void decreaseStock_shouldThrowWhenNotEnoughStock() {
        UUID id = UUID.randomUUID();
        Product p = new Product();
        p.setId(id);
        p.setName("Phone");
        p.setStock(1);
        p.setCreatedAt(Instant.now());
        p.setUpdatedAt(Instant.now());

        when(productRepository.findById(id)).thenReturn(Optional.of(p));
        assertThrows(NotEnoughStockException.class, () -> productService.decreaseStock(id, 2));
    }
}
