package ru.yandex.transfer_service.client;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.notification.model.NotificationDto;

@Component
@RequiredArgsConstructor
public class NotificationClient {

    private static final String TOPIC = "notifications";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String message) {
        kafkaTemplate.send(TOPIC, UUID.randomUUID().toString(), new NotificationDto().log(message));
    }
}
