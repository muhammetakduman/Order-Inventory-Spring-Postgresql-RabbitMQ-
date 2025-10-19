package com.muhammetakduman.order_inventory.repository;

import com.muhammetakduman.order_inventory.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
