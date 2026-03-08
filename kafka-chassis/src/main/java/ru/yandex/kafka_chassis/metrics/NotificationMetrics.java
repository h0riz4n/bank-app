package ru.yandex.kafka_chassis.metrics;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class NotificationMetrics {

    private final MeterRegistry meterRegistry;

    public NotificationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    } 

    public void incrementFailedNotification(String login) {
        Counter.builder("failed_notification")
            .tag("login", login)
            .register(meterRegistry)
            .increment();
    }
}
