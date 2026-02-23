package ru.yandex.account_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


import lombok.extern.slf4j.Slf4j;
import ru.notification.api.NotificationApi;
import ru.notification.client.ApiClient;
import ru.notification.model.NotificationDto;

@Slf4j
@Component
public class NotificationClient {

    private final String NOTIFICATION_BASE_PATH = "http://notification-service";
    private final NotificationApi notificationApi;

    public NotificationClient(RestClient restClient) {
        this.notificationApi = new NotificationApi(new ApiClient(restClient).setBasePath(NOTIFICATION_BASE_PATH));
    }

    public void notify(String message) {
        NotificationDto notification = new NotificationDto()
            .log(message);
        notificationApi.notification(notification);
    }
}
