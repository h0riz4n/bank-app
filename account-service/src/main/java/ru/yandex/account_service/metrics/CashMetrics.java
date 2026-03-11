package ru.yandex.account_service.metrics;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CashMetrics {

    private final MeterRegistry meterRegistry;

    public void incrementFailedWithdraw(String login) {
        Counter.builder("failed_withdraw")
            .tag("login", login)
            .register(meterRegistry)
            .increment();
    }
}
