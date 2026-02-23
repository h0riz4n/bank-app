package ru.yandex.notification_service.model.dto;

import jakarta.validation.constraints.NotEmpty;

public record NotificationDto(

    @NotEmpty
    String log

) {

}
