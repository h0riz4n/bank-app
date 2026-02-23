package ru.yandex.transfer_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import ru.notification.api.NotificationApi;
import ru.notification.client.ApiClient;
import ru.notification.model.NotificationDto;

@Component
public class NotificationClient {

    private final String notificationBaseUrl = "http://notification-service";
    private final NotificationApi notificationApi;

    public NotificationClient(RestClient restClient) {
        this.notificationApi = new NotificationApi(new ApiClient(restClient).setBasePath(notificationBaseUrl));
    }

    public void notify(String message) {
        NotificationDto notification = new NotificationDto();
        notification.setLog(message);
        notificationApi.notification(notification);
    }
}
