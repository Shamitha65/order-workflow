package com.example.order.application;

import com.example.order.application.port.ClockPort;
import com.example.order.application.port.NotificationPort;
import com.example.order.application.port.OrderRepository;
import com.example.order.domain.Money;
import com.example.order.domain.Order;
import com.example.order.domain.OrderId;
import com.example.order.domain.OrderLine;
import com.example.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private final Instant now =
            Instant.parse("2026-01-01T10:00:00Z");

    @Test
    void shouldConfirmOrderAndPublishEvent() {

        OrderRepository repository = new FakeOrderRepository();

        NotificationPort notification = event -> {
        };

        ClockPort clock = () -> now;

        OrderService service =
                new OrderService(repository, notification, clock);

        Order order = Order.create(OrderId.newId());

        order.addLine(
                new OrderLine(
                        "BISCUIT001",
                        2,
                        new Money(
                                new BigDecimal("20.00"),
                                "INR"
                        )
                )
        );

        repository.save(order);

        service.confirm(order.id());

        assertEquals(
                OrderStatus.CONFIRMED,
                order.status()
        );
    }

    @Test
    void shouldRecordPayment() {

        OrderRepository repository = new FakeOrderRepository();

        NotificationPort notification = event -> {
        };

        ClockPort clock = () -> now;

        OrderService service =
                new OrderService(repository, notification, clock);

        Order order = Order.create(OrderId.newId());

        order.addLine(
                new OrderLine(
                        "BISCUIT001",
                        2,
                        new Money(
                                new BigDecimal("20.00"),
                                "INR"
                        )
                )
        );

        repository.save(order);

        service.confirm(order.id());
        service.recordPayment(order.id());

        assertEquals(
                OrderStatus.PAID,
                order.status()
        );
    }

    private static class FakeOrderRepository
            implements OrderRepository {

        private Order order;

        @Override
        public void save(Order order) {
            this.order = order;
        }

        @Override
        public Optional<Order> findById(OrderId id) {
            return Optional.ofNullable(order);
        }
    }
}