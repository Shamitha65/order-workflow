package com.example.order.adapter;

import com.example.order.application.port.NotificationPort;
import com.example.order.domain.event.DomainEvent;

import java.util.ArrayList;
import java.util.List;

public class InMemoryNotificationAdapter implements NotificationPort {

    private final List<DomainEvent> events = new ArrayList<>();

    @Override
    public void notify(DomainEvent event) {
        events.add(event);
    }

    public List<DomainEvent> events() {
        return List.copyOf(events);
    }
}