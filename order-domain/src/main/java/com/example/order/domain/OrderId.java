package com.example.order.domain;

import java.util.Objects;
import java.util.UUID;

public record OrderId(UUID value) {

    public OrderId {
        Objects.requireNonNull(value, "Order ID cannot be null");
    }

    public static OrderId newId() {
        return new OrderId(UUID.randomUUID());
    }
}
