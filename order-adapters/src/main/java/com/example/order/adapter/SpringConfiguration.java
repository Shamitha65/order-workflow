package com.example.order.adapter;

import com.example.order.application.OrderService;
import com.example.order.application.port.ClockPort;
import com.example.order.application.port.NotificationPort;
import com.example.order.application.port.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {

    @Bean
    public OrderRepository orderRepository() {
        return new InMemoryOrderRepository();
    }

    @Bean
    public NotificationPort notificationPort() {
        return new InMemoryNotificationAdapter();
    }

    @Bean
    public ClockPort clockPort() {
        return new SystemClockAdapter();
    }

    @Bean
    public OrderService orderService(
            OrderRepository orderRepository,
            NotificationPort notificationPort,
            ClockPort clockPort
    ) {
        return new OrderService(
                orderRepository,
                notificationPort,
                clockPort
        );
    }
}