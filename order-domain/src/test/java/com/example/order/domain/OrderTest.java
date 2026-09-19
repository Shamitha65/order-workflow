package com.example.order.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private final Instant now = Instant.parse("2026-01-01T10:00:00Z");

    @Test
    void shouldConfirmOrder() {
        Order order = Order.create(OrderId.newId());

        order.addLine(
                new OrderLine(
                        "BISCUIT001",
                        2,
                        new Money(new BigDecimal("20.00"), "INR")
                )
        );

        order.confirm(now);

        assertEquals(OrderStatus.CONFIRMED, order.status());
    }

    @Test
    void shouldNotConfirmEmptyOrder() {
        Order order = Order.create(OrderId.newId());

        assertThrows(
                DomainException.class,
                () -> order.confirm(now)
        );
    }

    @Test
    void shouldRecordPayment() {
        Order order = Order.create(OrderId.newId());

        order.addLine(
                new OrderLine(
                        "BISCUIT001",
                        2,
                        new Money(new BigDecimal("20.00"), "INR")
                )
        );

        order.confirm(now);
        order.recordPayment(now);

        assertEquals(OrderStatus.PAID, order.status());
    }

    @Test
    void cancelledOrderCannotBePaid() {
        Order order = Order.create(OrderId.newId());

        order.addLine(
                new OrderLine(
                        "BISCUIT001",
                        1,
                        new Money(new BigDecimal("20.00"), "INR")
                )
        );

        order.cancel(now);

        assertThrows(
                DomainException.class,
                () -> order.recordPayment(now)
        );
    }

    @Test
    void paidOrderCannotBeCancelled() {
        Order order = Order.create(OrderId.newId());

        order.addLine(
                new OrderLine(
                        "BISCUIT001",
                        1,
                        new Money(new BigDecimal("20.00"), "INR")
                )
        );

        order.confirm(now);
        order.recordPayment(now);

        assertThrows(
                DomainException.class,
                () -> order.cancel(now)
        );
    }

    @Test
    void shouldCalculateOrderTotal() {
        Order order = Order.create(OrderId.newId());

        order.addLine(
                new OrderLine(
                        "BISCUIT001",
                        2,
                        new Money(new BigDecimal("20.00"), "INR")
                )
        );

        order.addLine(
                new OrderLine(
                        "CHIPS001",
                        3,
                        new Money(new BigDecimal("10.00"), "INR")
                )
        );

        assertEquals(
                new Money(new BigDecimal("70.00"), "INR"),
                order.total()
        );
    }
}