package ru.yandex.cash_service.service;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.account.model.AccountDto;
import ru.yandex.cash_service.client.CashClient;
import ru.yandex.cash_service.client.NotificationClient;
import ru.yandex.cash_service.model.dto.Cash;

@Service
@RequiredArgsConstructor
public class CashService {

    private final NotificationClient notificationClient;
    private final CashClient cashClient;

    public AccountDto cash(Cash cash) {
        var accountId = getAccountId(getCurrentAuth().getToken());
        var account = cashClient.cash(accountId, cash);
        notificationClient.notify("Изменён баланс счёта %s. Теперь баланс составляет %s".formatted(account.getId(), account.getAmount()));
        return account;
    }

    private UUID getAccountId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    private JwtAuthenticationToken getCurrentAuth() {
        return (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    } 
}
