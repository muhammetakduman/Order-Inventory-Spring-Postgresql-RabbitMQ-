package com.muhammetakduman.order_inventory.service;


import com.muhammetakduman.order_inventory.dto.order.OrderCreateRequest;
import com.muhammetakduman.order_inventory.dto.order.OrderResponse;
import com.muhammetakduman.order_inventory.mapper.OrderMapper;
import com.muhammetakduman.order_inventory.model.order.Order;
import com.muhammetakduman.order_inventory.model.order.OrderItem;
import com.muhammetakduman.order_inventory.model.product.Product;
import com.muhammetakduman.order_inventory.repository.OrderRepository;
import com.muhammetakduman.order_inventory.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;


    @Transactional
    public OrderResponse create(OrderCreateRequest request){
        Order order = new Order();
        //Bütün itemler için Product doğrula ve OrderItem Oluştur.(Db ilişkisi)
        for (OrderCreateRequest.Item item : request.items()){
            Product product = productRepository.findById((item.productId()))
                    .orElseThrow(()-> new EntityNotFoundException("Product not found : " + item.productId()));
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.quantity());
            order.addItem(orderItem);
        }
        order = orderRepository.save(order);
        return orderMapper.toResponse(order);
    }
    @Transactional(readOnly = true)
    public OrderResponse get(UUID id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Order Not Found :" + id));
        return orderMapper.toResponse(order);
    }

}
