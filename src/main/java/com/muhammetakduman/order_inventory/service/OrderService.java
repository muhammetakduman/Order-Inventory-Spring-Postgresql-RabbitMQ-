package com.muhammetakduman.order_inventory.service;

import com.muhammetakduman.order_inventory.dto.order.OrderCreateRequest;
import com.muhammetakduman.order_inventory.dto.order.OrderResponse;
import com.muhammetakduman.order_inventory.mapper.OrderMapper;
import com.muhammetakduman.order_inventory.messaging.dto.OrderCreatedEvent;
import com.muhammetakduman.order_inventory.messaging.producer.OrderEventsProducer;
import com.muhammetakduman.order_inventory.model.order.Order;
import com.muhammetakduman.order_inventory.model.order.OrderItem;
import com.muhammetakduman.order_inventory.model.product.Product;
import com.muhammetakduman.order_inventory.repository.OrderRepository;
import com.muhammetakduman.order_inventory.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final OrderEventsProducer orderEventsProducer;

    @Transactional
    public OrderResponse create(OrderCreateRequest request) {

        // 1) Order aggregate
        Order order = new Order();

        // 2) Tüm item’lar için ürün doğrula ve OrderItem ekle
        for (OrderCreateRequest.Item item : request.items()) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + item.productId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.quantity());

            order.addItem(orderItem); // önemli: addItem içinde item.setOrder(this) olmalı
        }

        // 3) Kaydet
        Order saved = orderRepository.save(order);

        // 4) (Gerekirse) yeniden yükle - lazy/eager durumlarını garantiye almak için
        Order finalSaved = saved;
        saved = orderRepository.findById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found after save: " + finalSaved.getId()));

        // 5) Event DTO hazırla
        List<OrderCreatedEvent.OrderItem> eventItems =
                saved.getItems() == null ? new ArrayList<>() :
                        saved.getItems().stream()
                                .map(i -> new OrderCreatedEvent.OrderItem(
                                        i.getProduct().getId(),
                                        i.getQuantity()))
                                .collect(Collectors.toList());

        OrderCreatedEvent event = new OrderCreatedEvent(saved.getId(), eventItems);

        // 6) DB commit'ten SONRA publish et (tutarlılık için)
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                orderEventsProducer.publishOrderCreated(event);
            }
        });

        // 7) API cevabı
        return orderMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public OrderResponse get(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order Not Found: " + id));
        return orderMapper.toResponse(order);
    }
}
