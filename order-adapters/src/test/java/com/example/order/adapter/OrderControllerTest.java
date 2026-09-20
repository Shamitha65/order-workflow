package com.example.order.adapter;

import com.example.order.adapter.controller.OrderController;
import com.example.order.application.OrderService;
import com.example.order.domain.DomainException;
import com.example.order.domain.Money;
import com.example.order.domain.Order;
import com.example.order.domain.OrderId;
import com.example.order.domain.OrderLine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void shouldCreateOrder() throws Exception {

        Order order = Order.create(OrderId.newId());

        when(orderService.createOrder())
                .thenReturn(order);

        mockMvc.perform(
                post("/api/orders")
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$.orderId")
                        .value(order.id().value().toString())
        )
        .andExpect(
                jsonPath("$.status")
                        .value("DRAFT")
        );
    }

    @Test
    void shouldAddOrderLine() throws Exception {

        Order order = Order.create(OrderId.newId());

        when(orderService.getOrder(order.id()))
                .thenReturn(order);

        mockMvc.perform(
                post("/api/orders/" + order.id().value() + "/lines")
                        .param("productCode", "BISCUIT001")
                        .param("quantity", "2")
                        .param("unitPrice", "20.00")
                        .param("currency", "INR")
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$.message")
                        .value("Order line added successfully")
        );
    }

    @Test
    void shouldGetOrder() throws Exception {

        Order order = Order.create(OrderId.newId());

        when(orderService.getOrder(order.id()))
                .thenReturn(order);

        mockMvc.perform(
                get("/api/orders/" + order.id().value())
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$.orderId")
                        .value(order.id().value().toString())
        )
        .andExpect(
                jsonPath("$.status")
                        .value("DRAFT")
        )
        .andExpect(
                jsonPath("$.currency")
                        .value("INR")
        );
    }

    @Test
    void shouldConfirmOrder() throws Exception {

        Order order = Order.create(OrderId.newId());

        when(orderService.getOrder(order.id()))
                .thenReturn(order);

        mockMvc.perform(
                post("/api/orders/" + order.id().value() + "/confirm")
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$.message")
                        .value("Order confirmed successfully")
        );
    }

    @Test
    void shouldRecordPayment() throws Exception {

        Order order = Order.create(OrderId.newId());

        when(orderService.getOrder(order.id()))
                .thenReturn(order);

        mockMvc.perform(
                post("/api/orders/" + order.id().value() + "/payment")
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$.message")
                        .value("Payment recorded successfully")
        );
    }

    @Test
    void shouldCancelOrder() throws Exception {

        Order order = Order.create(OrderId.newId());

        when(orderService.getOrder(order.id()))
                .thenReturn(order);

        mockMvc.perform(
                post("/api/orders/" + order.id().value() + "/cancel")
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$.message")
                        .value("Order cancelled successfully")
        );
    }

    @Test
    void shouldReturnBadRequestWhenCancellingPaidOrder()
            throws Exception {

        Order order = Order.create(OrderId.newId());

        doThrow(
                new DomainException(
                        "Paid orders cannot be cancelled"
                )
        )
        .when(orderService)
        .cancel(order.id());

        mockMvc.perform(
                post("/api/orders/" + order.id().value() + "/cancel")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
                jsonPath("$.error")
                        .value("Paid orders cannot be cancelled")
        );
    }
}