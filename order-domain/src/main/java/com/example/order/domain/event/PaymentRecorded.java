package com.example.order.domain.event;

import com.example.order.domain.OrderId;

import java.time.Instant;

public record PaymentRecorded(
        OrderId orderId,
        Instant occurredAt
) implements DomainEvent {
}