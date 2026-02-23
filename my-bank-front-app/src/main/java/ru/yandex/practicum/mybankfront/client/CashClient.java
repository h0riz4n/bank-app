package ru.yandex.practicum.mybankfront.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.mybankfront.model.Account;
import ru.yandex.practicum.mybankfront.model.Cash;

@Component
@RequiredArgsConstructor
public class CashClient {

    private final RestClient restClient;

    public ResponseEntity<Account> cash(String token, Cash dto) {
        return restClient
            .put()
            .uri("/cash-service/api/cash")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
            .body(dto)
            .retrieve()
            .toEntity(Account.class);
    }
}
