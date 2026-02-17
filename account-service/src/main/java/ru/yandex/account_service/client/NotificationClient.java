package ru.yandex.account_service.client;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.notification.api.NotificationApi;
import ru.notification.model.NotificationDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationClient {

    private final NotificationApi notificationApi;

    public void notify(String message) {
        NotificationDto notification = new NotificationDto();
        notification.setLog(message);
        notificationApi.notification(notification);
    }
}
