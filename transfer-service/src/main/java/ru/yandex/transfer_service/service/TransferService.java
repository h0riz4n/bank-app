package ru.yandex.transfer_service.service;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.account.model.TransferDto;
import ru.notification.model.NotificationDto;
import ru.yandex.kafka_chassis.client.NotificationClient;
import ru.yandex.transfer_service.client.TransferClient;
import ru.yandex.transfer_service.model.dto.RecepientDto;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final NotificationClient notificationClient;
    private final TransferClient transferClient;

    public void transfer(RecepientDto dto) {
        var transfer = new TransferDto()
            .amount(dto.amount())
            .recipientId(dto.id())
            .senderId(getAccountId(getCurrentAuth().getToken()));

        transferClient.transfer(transfer);
        var message = toNotification(
            "Был произведён перевод на сумму %s со счёта %s на %s".formatted(
                transfer.getAmount(), 
                transfer.getSenderId(), 
                transfer.getRecipientId()
            )
        );
        notificationClient.send(transfer.getSenderId().toString(), transfer.getRecipientId().toString(), message);
    }

    private UUID getAccountId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    private JwtAuthenticationToken getCurrentAuth() {
        return (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    private NotificationDto toNotification(String message) {
        return new NotificationDto().log(message);
    }
}
