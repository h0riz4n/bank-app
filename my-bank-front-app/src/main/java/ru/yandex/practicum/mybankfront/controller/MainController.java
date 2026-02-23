package ru.yandex.practicum.mybankfront.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.mybankfront.model.dto.CashAction;
import ru.yandex.practicum.mybankfront.service.FrontService;

import java.time.LocalDate;

/**
 * Контроллер main.html.
 *
 * Используемая модель для main.html:
 *      model.addAttribute("name", name);
 *      model.addAttribute("birthdate", birthdate.format(DateTimeFormatter.ISO_DATE));
 *      model.addAttribute("sum", sum);
 *      model.addAttribute("accounts", accounts);
 *      model.addAttribute("errors", errors);
 *      model.addAttribute("info", info);
 *
 * Поля модели:
 *      name - Фамилия Имя текущего пользователя, String (обязательное)
 *      birthdate - дата рождения текущего пользователя, String в формате 'YYYY-MM-DD' (обязательное)
 *      sum - сумма на счету текущего пользователя, Integer (обязательное)
 *      accounts - список аккаунтов, которым можно перевести деньги, List<AccountDto> (обязательное)
 *      errors - список ошибок после выполнения действий, List<String> (не обязательное)
 *      info - строка успешности после выполнения действия, String (не обязательное)
 *
 * С примерами использования можно ознакомиться в тестовом классе заглушке AccountStub
 */
@Controller
@RequiredArgsConstructor
public class MainController {

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClient;
    private final FrontService frontService;

    /**
     * GET /.
     * Редирект на GET /account
     */
    @GetMapping
    public String index() {
        return "redirect:/account";
    }

    @GetMapping("/account")
    public String getAccount(Model model, @RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient authorizedClient) {
        frontService.getAccount(model, authorizedClient.getAccessToken().getTokenValue());
        return "main";
    }

    @PostMapping("/account")
    public String editAccount(
        Model model,
        @RequestParam("name") String name,
        @RequestParam("birthdate") LocalDate birthdate,
        @RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient authorizedClient
    ) {
        frontService.updateAccount(model, name, birthdate, authorizedClient.getAccessToken().getTokenValue());
        return "main";
    }

    @PostMapping("/cash")
    public String editCash(
        Model model,
        @RequestParam("value") int value,
        @RequestParam("action") CashAction action,
        @RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient authorizedClient
    ) {
        frontService.cash(model, authorizedClient.getAccessToken().getTokenValue(), value, action);
        return "main";
    }

    @PostMapping("/transfer")
    public String transfer(
        Model model,
        @RequestParam("value") int value,
        @RequestParam("login") String login,
        @RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient authorizedClient
    ) {
        frontService.transfer(model, authorizedClient.getAccessToken().getTokenValue(), value, login);
        return "main";
    }
}
