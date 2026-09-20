# Order Workflow

A modular Java application that demonstrates an order lifecycle using Domain-Driven Design (DDD) and Clean Architecture principles.

## Order Lifecycle

DRAFT → CONFIRMED → PAID

DRAFT → CANCELLED

CONFIRMED → CANCELLED

A paid order cannot be cancelled.

## Features

* Create an order
* Add order items
* Calculate order total
* Confirm an order
* Record payment
* Cancel an order
* Validate business rules
* Generate domain events
* In-memory order repository
* REST API
* Global exception handling
* Unit and controller testing
* Executable Spring Boot JAR

## Project Structure

order-workflow/

├── order-domain/

│   └── Domain models, business rules and domain events

├── order-application/

│   └── Application services and ports

├── order-adapters/

│   └── REST controllers and infrastructure implementations

└── docs/

└── adr/

    └── Architecture Decision Records


### Modules

* order-domain – Business rules, domain models and domain events
* order-application – Application services and ports
* order-adapters – REST API and infrastructure implementations
* docs/adr – Architecture decisions

## Technologies

* Java 17
* Maven
* Spring Boot 3.3.5
* JUnit 5
* Git
* GitHub

## REST API

### Create Order

POST /api/orders

### Add Order Item

POST /api/orders/{orderId}/lines

Parameters:

productCode

quantity

unitPrice

currency

### Get Order

GET /api/orders/{orderId}

### Confirm Order

POST /api/orders/{orderId}/confirm

### Record Payment

POST /api/orders/{orderId}/payment

### Cancel Order

POST /api/orders/{orderId}/cancel

### Health Check

GET /api/health

## Business Rules

The application enforces the following rules:

* An order starts in DRAFT status.
* Items can only be added to a draft order.
* An empty order cannot be confirmed.
* Only a draft order can be confirmed.
* Only a confirmed order can be paid.
* A paid order cannot be cancelled.
* An already cancelled order cannot be cancelled again.
* Order amounts cannot be negative.
* Order item quantities must be greater than zero.
* Currency must be valid and consistent when calculating totals.

## Domain Events

The domain generates events when important state changes occur:

* OrderConfirmed
* PaymentRecorded
* OrderCancelled

These events are passed to the notification port through the application service.

## Testing

The project includes automated tests for:

* Domain business rules
* Order state transitions
* Order total calculation
* Application service operations
* REST controller operations
* Exception handling

Run all tests with:

mvn clean test

## Build the Project

To build all modules:

mvn clean install

A successful build should display:

BUILD SUCCESS

## Run the Application

Run the executable JAR:

java -jar .\order-adapters\target\order-adapters-1.0-SNAPSHOT.jar

The application runs on:

http://localhost:8081

### Health Check

Open:

http://localhost:8081/api/health

Expected response:

Order Workflow API is running

## Example Workflow

A typical order workflow is:

1. Create Order
2. Add Order Item
3. Confirm Order
4. Record Payment
5. Order becomes PAID

The application prevents invalid operations such as cancelling a paid order.

## Architecture

The project follows a layered modular architecture:

REST API
↓
order-adapters
↓
order-application
↓
order-domain

The domain layer contains the core business rules and does not depend on Spring Boot or infrastructure code.

## Architecture Decisions

Architecture decisions are documented in:

docs/adr/

Current decisions include:

* Domain purity
* Money as a value object
* Order state transition rules

## Author

Order Workflow – Java/Maven modular application demonstrating Clean Architecture, Domain-Driven Design, business rules, REST APIs and automated testing.
