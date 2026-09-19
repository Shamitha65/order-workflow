package com.example.order.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount, String currency) {

    public Money {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Amount cannot be negative");
        }

        if (currency.isBlank()) {
            throw new DomainException("Currency cannot be blank");
        }

        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
        currency = currency.toUpperCase();
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money other) {
        if (!currency.equals(other.currency)) {
            throw new DomainException("Currencies must match");
        }

        return new Money(amount.add(other.amount), currency);
    }

    public Money multiply(int quantity) {
        return new Money(
                amount.multiply(BigDecimal.valueOf(quantity)),
                currency
        );
    }
}