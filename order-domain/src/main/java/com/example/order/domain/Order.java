package com.example.order.domain;

import com.example.order.domain.event.DomainEvent;
import com.example.order.domain.event.OrderCancelled;
import com.example.order.domain.event.OrderConfirmed;
import com.example.order.domain.event.PaymentRecorded;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {

    private final OrderId id;
    private final List<OrderLine> lines = new ArrayList<>();
    private final List<DomainEvent> events = new ArrayList<>();

    private OrderStatus status;

    private Order(OrderId id) {
        this.id = Objects.requireNonNull(id);
        this.status = OrderStatus.DRAFT;
    }

    public static Order create(OrderId id) {
        return new Order(id);
    }

    public OrderId id() {
        return id;
    }

    public OrderStatus status() {
        return status;
    }

    public List<OrderLine> lines() {
        return List.copyOf(lines);
    }

    public void addLine(OrderLine line) {
        if (status != OrderStatus.DRAFT) {
            throw new DomainException(
                    "Items can only be added to a draft order"
            );
        }

        lines.add(Objects.requireNonNull(line));
    }

    public Money total() {
        if (lines.isEmpty()) {
            return Money.zero("INR");
        }

        Money total = Money.zero(lines.get(0).unitPrice().currency());

        for (OrderLine line : lines) {
            total = total.add(line.total());
        }

        return total;
    }

    public void confirm(Instant now) {
        if (status != OrderStatus.DRAFT) {
            throw new DomainException(
                    "Only draft orders can be confirmed"
            );
        }

        if (lines.isEmpty()) {
            throw new DomainException(
                    "Cannot confirm an empty order"
            );
        }

        status = OrderStatus.CONFIRMED;

        events.add(new OrderConfirmed(id, now));
    }

    public void recordPayment(Instant now) {
        if (status != OrderStatus.CONFIRMED) {
            throw new DomainException(
                    "Only confirmed orders can be paid"
            );
        }

        status = OrderStatus.PAID;

        events.add(new PaymentRecorded(id, now));
    }

    public void cancel(Instant now) {
        if (status == OrderStatus.PAID) {
            throw new DomainException(
                    "Paid orders cannot be cancelled"
            );
        }

        if (status == OrderStatus.CANCELLED) {
            throw new DomainException(
                    "Order is already cancelled"
            );
        }

        status = OrderStatus.CANCELLED;

        events.add(new OrderCancelled(id, now));
    }

    public List<DomainEvent> pullEvents() {
        List<DomainEvent> result = List.copyOf(events);
        events.clear();
        return result;
    }
}