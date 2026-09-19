package com.example.order.application;

import com.example.order.application.port.ClockPort;
import com.example.order.application.port.NotificationPort;
import com.example.order.application.port.OrderRepository;
import com.example.order.domain.Order;
import com.example.order.domain.OrderId;
import com.example.order.domain.event.DomainEvent;

public class OrderService {

    private final OrderRepository orderRepository;
    private final NotificationPort notificationPort;
    private final ClockPort clockPort;

    public OrderService(
            OrderRepository orderRepository,
            NotificationPort notificationPort,
            ClockPort clockPort
    ) {
        this.orderRepository = orderRepository;
        this.notificationPort = notificationPort;
        this.clockPort = clockPort;
    }

    public void confirm(OrderId orderId) {
        Order order = getOrder(orderId);

        order.confirm(clockPort.now());

        saveAndPublish(order);
    }

    public void recordPayment(OrderId orderId) {
        Order order = getOrder(orderId);

        order.recordPayment(clockPort.now());

        saveAndPublish(order);
    }

    public void cancel(OrderId orderId) {
        Order order = getOrder(orderId);

        order.cancel(clockPort.now());

        saveAndPublish(order);
    }

    private Order getOrder(OrderId orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Order not found")
                );
    }

    private void saveAndPublish(Order order) {
        orderRepository.save(order);

        for (DomainEvent event : order.pullEvents()) {
            notificationPort.notify(event);
        }
    }
}