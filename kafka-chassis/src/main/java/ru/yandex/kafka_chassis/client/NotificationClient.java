package ru.yandex.kafka_chassis.client;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import ru.yandex.kafka_chassis.metrics.NotificationMetrics;

@Component
public class NotificationClient {

    private static final String TOPIC = "notifications";
    private final NotificationMetrics notificationMetrics;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public NotificationClient(NotificationMetrics notificationMetrics, KafkaTemplate<String, Object> kafkaTemplate) {
        this.notificationMetrics = notificationMetrics;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String login, Object message) {
        kafkaTemplate.send(TOPIC, UUID.randomUUID().toString(), message)
            .exceptionally(ex -> {
                notificationMetrics.incrementFailedNotification(login);
                return null;
            });
    }

    public void send(String sender, String recipient, Object message) {
        kafkaTemplate.send(TOPIC, UUID.randomUUID().toString(), message)
            .exceptionally(ex -> {
                notificationMetrics.incrementFailedNotification(sender);
                notificationMetrics.incrementFailedNotification(recipient);
                return null;
            });
    }
}
