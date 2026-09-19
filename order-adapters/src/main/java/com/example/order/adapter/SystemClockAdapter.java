package com.example.order.adapter;

import com.example.order.application.port.ClockPort;

import java.time.Instant;

public class SystemClockAdapter implements ClockPort {

    @Override
    public Instant now() {
        return Instant.now();
    }
}