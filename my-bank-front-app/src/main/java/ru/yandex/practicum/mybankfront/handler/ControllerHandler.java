package ru.yandex.practicum.mybankfront.handler;

import org.springframework.http.ProblemDetail;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;


import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.mybankfront.exception.ApiServiceException;
import ru.yandex.practicum.mybankfront.service.FrontService;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerHandler {

    private final FrontService frontService;

    @ExceptionHandler(HttpClientErrorException.class)
    public final String handleHttpClientErrorException(
        HttpClientErrorException ex, 
        Model model,
        @RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient authorizedClient
    ) {
        frontService.getAccount(model, authorizedClient.getAccessToken().getTokenValue());
        model.addAttribute("errors", ex.getResponseBodyAs(ProblemDetail.class).getDetail());
        model.addAttribute("info", null);
        return "main";
    }

    @ExceptionHandler(ApiServiceException.class)
    public final String handleApiServiceException(
        ApiServiceException ex, 
        Model model,
        @RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient authorizedClient
    ) {
        frontService.getAccount(model, authorizedClient.getAccessToken().getTokenValue());
        model.addAttribute("errors", ex.getMessage());
        model.addAttribute("info", null);
        return "main";
    }
}
