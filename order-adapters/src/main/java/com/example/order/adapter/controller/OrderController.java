package com.example.order.adapter.controller;

import com.example.order.application.OrderService;
import com.example.order.domain.Order;
import com.example.order.domain.OrderId;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Map<String, Object> createOrder() {

        Order order = orderService.createOrder();

        return Map.of(
                "orderId", order.id().value().toString(),
                "status", order.status().name()
        );
    }

    @PostMapping("/{orderId}/lines")
    public Map<String, String> addLine(
            @PathVariable("orderId") UUID orderId,
            @RequestParam("productCode") String productCode,
            @RequestParam("quantity") int quantity,
            @RequestParam("unitPrice") BigDecimal unitPrice,
            @RequestParam("currency") String currency
    ) {

        orderService.addLine(
                new OrderId(orderId),
                productCode,
                quantity,
                unitPrice,
                currency
        );

        return Map.of(
                "message", "Order line added successfully"
        );
    }

    @GetMapping("/{orderId}")
    public Map<String, Object> getOrder(
            @PathVariable("orderId") UUID orderId
    ) {

        Order order = orderService.getOrder(
                new OrderId(orderId)
        );

        return Map.of(
                "orderId", order.id().value().toString(),
                "status", order.status().name(),
                "total", order.total().amount(),
                "currency", order.total().currency(),
                "lines", order.lines()
        );
    }

    @PostMapping("/{orderId}/confirm")
    public Map<String, String> confirmOrder(
            @PathVariable("orderId") UUID orderId
    ) {

        orderService.confirm(
                new OrderId(orderId)
        );

        return Map.of(
                "message", "Order confirmed successfully"
        );
    }

    @PostMapping("/{orderId}/payment")
    public Map<String, String> recordPayment(
            @PathVariable("orderId") UUID orderId
    ) {

        orderService.recordPayment(
                new OrderId(orderId)
        );

        return Map.of(
                "message", "Payment recorded successfully"
        );
    }

    @PostMapping("/{orderId}/cancel")
    public Map<String, String> cancelOrder(
            @PathVariable("orderId") UUID orderId
    ) {

        orderService.cancel(
                new OrderId(orderId)
        );

        return Map.of(
                "message", "Order cancelled successfully"
        );
    }
}