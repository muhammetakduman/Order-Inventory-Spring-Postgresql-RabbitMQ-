package com.muhammetakduman.order_inventory;

import com.muhammetakduman.order_inventory.dto.order.OrderCreateRequest;
import com.muhammetakduman.order_inventory.dto.order.OrderResponse;
import com.muhammetakduman.order_inventory.mapper.OrderMapper;
import com.muhammetakduman.order_inventory.model.order.Order;
import com.muhammetakduman.order_inventory.model.order.OrderItem;
import com.muhammetakduman.order_inventory.model.product.Product;
import com.muhammetakduman.order_inventory.repository.OrderRepository;
import com.muhammetakduman.order_inventory.repository.ProductRepository;
import com.muhammetakduman.order_inventory.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private OrderMapper mapper;
    private OrderService orderService;

    @BeforeEach
    void setup() {
        orderRepository = mock(OrderRepository.class);
        productRepository = mock(ProductRepository.class);
        mapper = new OrderMapper();
        orderService = new OrderService(orderRepository, productRepository, mapper /*, producer yok şimdilik */);
    }

    @Test
    void create_shouldSaveOrderAndReturnResponse() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Laptop");
        product.setStock(10);
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            // id simülasyonu
            try {
                java.lang.reflect.Field idF = Order.class.getDeclaredField("id");
                idF.setAccessible(true);
                idF.set(o, UUID.randomUUID());
            } catch (Exception ignored) {}
            return o;
        });

        OrderCreateRequest.Item item = new OrderCreateRequest.Item(productId, 2);
        OrderCreateRequest req = new OrderCreateRequest(List.of(item));

        OrderResponse resp = orderService.create(req);

        assertNotNull(resp.id());
        assertEquals(1, resp.items().size());
        assertEquals(productId, resp.items().get(0).productId());
        assertEquals(2, resp.items().get(0).quantity());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void create_shouldThrowWhenProductNotFound() {
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        OrderCreateRequest req = new OrderCreateRequest(List.of(new OrderCreateRequest.Item(productId, 1)));
        assertThrows(EntityNotFoundException.class, () -> orderService.create(req));
    }
}
