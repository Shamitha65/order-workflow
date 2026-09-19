package com.example.order.application.port;

import java.time.Instant;

public interface ClockPort {

    Instant now();
}