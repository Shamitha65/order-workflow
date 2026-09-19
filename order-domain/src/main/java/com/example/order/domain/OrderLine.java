package com.example.order.domain;

import java.util.Objects;

public record OrderLine(
        String productCode,
        int quantity,
        Money unitPrice
) {

    public OrderLine {
        if (productCode == null || productCode.isBlank()) {
            throw new DomainException("Product code cannot be blank");
        }

        if (quantity <= 0) {
            throw new DomainException("Quantity must be greater than zero");
        }

        Objects.requireNonNull(unitPrice, "Unit price cannot be null");
    }

    public Money total() {
        return unitPrice.multiply(quantity);
    }
}
