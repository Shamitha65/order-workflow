# ADR 002: Use Money as a Value Object

## Decision

Money is represented using amount and currency together.

## Reason

This prevents invalid monetary operations and keeps currency
handling inside the domain model.

## Consequence

Orders and order lines use the Money type instead of raw numbers.