package com.example.order.adapter;

import com.example.order.application.OrderService;
import com.example.order.domain.Order;
import com.example.order.domain.OrderId;
import com.example.order.domain.OrderLine;
import com.example.order.domain.Money;

import java.math.BigDecimal;

public class Main {

   public static void main(String[] args) {

        // Create adapters
        InMemoryOrderRepository repository =
                new InMemoryOrderRepository();

        InMemoryNotificationAdapter notification =
                new InMemoryNotificationAdapter();

        SystemClockAdapter clock =
                new SystemClockAdapter();

        // Create application service
        OrderService service =
                new OrderService(repository, notification, clock);

        // Create an order
        Order order = Order.create(OrderId.newId());

        System.out.println("Order created: " + order.status());

        // Add products
        order.addLine(
                new OrderLine(
                        "BISCUIT001",
                        2,
                        new Money(
                                new BigDecimal("20.00"),
                                "INR"
                        )
                )
        );

        order.addLine(
                new OrderLine(
                        "CHIPS001",
                        3,
                        new Money(
                                new BigDecimal("10.00"),
                                "INR"
                        )
                )
        );

        repository.save(order);

        System.out.println("Order total: " + order.total().amount()
                + " " + order.total().currency());

        // Confirm
        service.confirm(order.id());

        System.out.println("Order confirmed: "
                + order.status());

        // Payment
        service.recordPayment(order.id());

        System.out.println("Payment recorded: "
                + order.status());

        // Notifications
        System.out.println("Events generated: "
                + notification.events().size());

        System.out.println("Order workflow completed!");
    }
}