package ru.yandex.api_gateway.scheduler;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ScheduleService {

    private final RestClient restClient;

    public ScheduleService(@Value("${server.port}") int port) {
        URI uri = UriComponentsBuilder
            .fromUriString("http://localhost:{port}/actuator/refresh")
            .build(port);
        this.restClient = RestClient.create(uri);
    }

    @Scheduled(fixedRate = 5000)
    public void refresh() {
        restClient
            .post()
            .retrieve()
            .toBodilessEntity();
    }
}
