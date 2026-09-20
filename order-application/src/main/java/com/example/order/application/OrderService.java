package com.example.order.application;

import com.example.order.application.port.ClockPort;
import com.example.order.application.port.NotificationPort;
import com.example.order.application.port.OrderRepository;
import com.example.order.domain.Money;
import com.example.order.domain.Order;
import com.example.order.domain.OrderId;
import com.example.order.domain.OrderLine;
import com.example.order.domain.event.DomainEvent;

import java.math.BigDecimal;

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

    public Order createOrder() {
        Order order = Order.create(OrderId.newId());

        orderRepository.save(order);

        return order;
    }

    public void addLine(
            OrderId orderId,
            String productCode,
            int quantity,
            BigDecimal unitPrice,
            String currency
    ) {
        Order order = getOrder(orderId);

        Money money = new Money(unitPrice, currency);

        OrderLine line = new OrderLine(
                productCode,
                quantity,
                money
        );

        order.addLine(line);

        orderRepository.save(order);
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

    public Order getOrder(OrderId orderId) {
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