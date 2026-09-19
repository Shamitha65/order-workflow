package com.example.order.application.port;

import com.example.order.domain.Order;
import com.example.order.domain.OrderId;

import java.util.Optional;

public interface OrderRepository {

    void save(Order order);

    Optional<Order> findById(OrderId id);
}