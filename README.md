# Order Workflow

A modular Java application that demonstrates an order lifecycle using
domain-driven design and clean architecture principles.

## Order Lifecycle

DRAFT → CONFIRMED → PAID

DRAFT → CANCELLED

CONFIRMED → CANCELLED

## Features

- Create an order
- Add order items
- Calculate order total
- Confirm an order
- Record payment
- Cancel an order
- Validate business rules
- Generate domain events
- In-memory order repository
- Unit testing

## Project Structure

- `order-domain` – Business rules and domain models
- `order-application` – Application services and ports
- `order-adapters` – Infrastructure implementations
- `docs/adr` – Architecture decisions

## Technologies

- Java 17
- Maven
- JUnit 5
- Git
- GitHub

## Running the Project

Build the project:

```bash
mvn clean package