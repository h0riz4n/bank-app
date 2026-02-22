package ru.yandex.practicum.mybankfront.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.mybankfront.model.Account;
import ru.yandex.practicum.mybankfront.model.UpdateAccount;

@Component
@RequiredArgsConstructor
public class AccountClient {

    private final RestClient restClient;

    public ResponseEntity<Account> getAccount(String token) {
        return restClient
            .get()
            .uri("/account-service/api/accounts/me")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
            .retrieve()
            .toEntity(Account.class);
    }

    public ResponseEntity<Account> updateAccount(String token, UpdateAccount dto) {
        return restClient
            .put()
            .uri("/account-service/api/accounts")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
            .body(dto)
            .retrieve()
            .toEntity(Account.class);
    }

    public ResponseEntity<List<Account>> getAccounts(String token) {
        return restClient
            .get()
            .uri("/account-service/api/accounts")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
            .retrieve()
            .toEntity(new ParameterizedTypeReference<List<Account>>() {});
    }
}
