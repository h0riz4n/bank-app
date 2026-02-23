package ru.yandex.notification_service.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.notification_service.model.dto.NotificationDto;

@Slf4j
@Validated
@RestController
@Tag(name = "Notification")
@SecurityRequirement(name = "OAuth")
@RequestMapping(path = "/api/notification")
public class NotificationController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> notification(@RequestBody @Valid NotificationDto dto) {
        log.info(dto.log());
        return ResponseEntity.noContent().build();
    }
}
