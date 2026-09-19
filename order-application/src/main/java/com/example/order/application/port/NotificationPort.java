package com.example.order.application.port;

import com.example.order.domain.event.DomainEvent;

public interface NotificationPort {

    void notify(DomainEvent event);
}