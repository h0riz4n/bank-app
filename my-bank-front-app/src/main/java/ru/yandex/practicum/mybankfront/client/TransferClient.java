package ru.yandex.practicum.mybankfront.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.mybankfront.model.Transfer;

@Component
@RequiredArgsConstructor
public class TransferClient {

    private final RestClient restClient;

    public ResponseEntity<Void> transfer(String token, Transfer dto) {
        return restClient
            .post()
            .uri("/transfers-service/api/transfer")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
            .body(dto)
            .retrieve()
            .toBodilessEntity();
    }
}
