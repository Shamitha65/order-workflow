# ADR 003: Define Order State Transitions

## Decision

An order follows these state transitions:

DRAFT -> CONFIRMED -> PAID

DRAFT -> CANCELLED

CONFIRMED -> CANCELLED

## Rules

A PAID order cannot be cancelled.

A CANCELLED order cannot be paid.

A DRAFT order cannot be paid.