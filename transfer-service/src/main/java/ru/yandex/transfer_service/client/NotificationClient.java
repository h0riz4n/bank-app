package ru.yandex.transfer_service.client;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import ru.notification.api.NotificationApi;
import ru.notification.client.ApiClient;
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
