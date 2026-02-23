package ru.yandex.cash_service.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebConfiguration {

    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public RestClient restClient(RestClient.Builder builder, OAuth2AuthorizedClientManager manager) {
        return builder
            .requestInterceptor((request, body, execution) -> {
                OAuth2AuthorizeRequest authRequest = OAuth2AuthorizeRequest.withClientRegistrationId("notification-client")
                    .principal("account-service")
                    .build();
                OAuth2AuthorizedClient client = manager.authorize(authRequest);
                request.getHeaders().setBearerAuth(client.getAccessToken().getTokenValue());
                return execution.execute(request, body);
            })
            .defaultStatusHandler(
                status -> status.is2xxSuccessful(),
                (request, response) -> {
                    log.debug("{} - {} - {}", request.getMethod(), request.getURI(), response.getStatusCode());
            })
            .build();
    }
}
