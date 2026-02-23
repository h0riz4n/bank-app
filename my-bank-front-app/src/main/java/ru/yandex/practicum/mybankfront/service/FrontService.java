package ru.yandex.practicum.mybankfront.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.mybankfront.client.AccountClient;
import ru.yandex.practicum.mybankfront.client.CashClient;
import ru.yandex.practicum.mybankfront.client.TransferClient;
import ru.yandex.practicum.mybankfront.exception.ApiServiceException;
import ru.yandex.practicum.mybankfront.model.Account;
import ru.yandex.practicum.mybankfront.model.Cash;
import ru.yandex.practicum.mybankfront.model.Transfer;
import ru.yandex.practicum.mybankfront.model.UpdateAccount;
import ru.yandex.practicum.mybankfront.model.dto.AccountDto;
import ru.yandex.practicum.mybankfront.model.dto.CashAction;

@Slf4j
@Service
@RequiredArgsConstructor
public class FrontService {

    private final CashClient cashClient;
    private final TransferClient transferClient;
    private final AccountClient accountClient;

    public void getAccount(Model model, String token) {
        var account = accountClient.getAccount(token).getBody();
        fillModel(model, account, getAccounts(token));
    }

    public void updateAccount(Model model, String name, LocalDate birthDate, String token) {
        var firstName = name.split(" ")[1];
        var lastName = name.split(" ")[0];
        var account = accountClient.updateAccount(token, new UpdateAccount(firstName, lastName, birthDate))
            .getBody();
        fillModel(model, account, getAccounts(token));
    }

    public void cash(Model model, String token, int value, CashAction action) {
        var account = cashClient.cash(token, new Cash(action, BigDecimal.valueOf(value)))
            .getBody();
        fillModel(model, account, getAccounts(token));
    }

    public void transfer(Model model, String token, int value, String login) {
        var accounts = accountClient.getAccounts(token).getBody();
        Account recipient = accounts.stream()
            .filter(current -> current.login().equals(login))
            .findFirst()
            .orElseThrow(() -> new ApiServiceException("Пользователь не найден"));
        
        transferClient.transfer(token, new Transfer(recipient.id(), BigDecimal.valueOf(value)));
        fillModel(model, accountClient.getAccount(token).getBody(), toAccountDto(accounts));
    }

    private void fillModel(Model model, Account account, List<AccountDto> accounts) {
        model.addAttribute("name", "%s %s".formatted(account.lastName(), account.firstName()));
        model.addAttribute("birthdate", account.birthDate().format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("sum", account.amount());
        model.addAttribute("accounts", accounts);
        model.addAttribute("errors", null);
        model.addAttribute("info", "Операция прошла успешно");
    }

    private List<AccountDto> getAccounts(String token) {
        return accountClient.getAccounts(token)
            .getBody()
            .stream()
            .map(acc -> new AccountDto(acc.login(), "%s %s".formatted(acc.lastName(), acc.firstName())))
            .toList();
    }

    private List<AccountDto> toAccountDto(List<Account> accounts) {
        return accounts
            .stream()
            .map(acc -> new AccountDto(acc.login(), "%s %s".formatted(acc.lastName(), acc.firstName())))
            .toList();
    }
}
