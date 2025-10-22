package com.muhammetakduman.order_inventory.model.order;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CREATED;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL, fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false,updatable = false)
    private Instant createdAt = Instant.now();

    public void addItem(OrderItem item){
        item.setOrder(this);
        this.items.add(item);
    }

}
