# ADR 001: Keep the Domain Layer Pure

## Decision

The domain layer must not depend on frameworks, databases, HTTP,
or external infrastructure.

## Reason

This keeps the business rules independent and easier to test.

## Consequence

Database and external-system implementations belong in adapters.