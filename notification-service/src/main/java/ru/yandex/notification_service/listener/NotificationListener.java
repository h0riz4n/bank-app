package ru.yandex.notification_service.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.notification_service.model.dto.NotificationDto;

@Slf4j
@Component
public class NotificationListener {

    @KafkaListener(topics = "notifications", groupId = "bank-app")
    public void log(NotificationDto notification) {
        log.info(notification.log());
    }
}
