package ru.yandex.account_service.metrics;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransferMetrics {

    private final MeterRegistry meterRegistry;

    public void incrementFailedTransfers(String login) {
        Counter.builder("failed_transfers")
            .tag("login", login)
            .register(meterRegistry)
            .increment();
    }
}
