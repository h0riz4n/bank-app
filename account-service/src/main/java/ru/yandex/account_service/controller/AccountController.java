package ru.yandex.account_service.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.yandex.account_service.mapper.AccountMapper;
import ru.yandex.account_service.model.dto.AccountDto;
import ru.yandex.account_service.model.view.AccountView;
import ru.yandex.account_service.service.AccountService;

@Validated
@RestController
@RequestMapping(path = "/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping(path = "/me")
    public ResponseEntity<AccountDto> getAccount() {
        return ResponseEntity.ok(accountMapper.toDto(accountService.getAccount()));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> updateAccount(@RequestBody @Valid @JsonView(AccountView.UpdateProfile.class) AccountDto dto) {
        var account = accountService.update(dto.getFirstName(), dto.getLastName(), dto.getBirthDate());
        return ResponseEntity.ok(accountMapper.toDto(account));
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAccounts() {
        var accounts = accountService.getAll()
            .stream()
            .map(accountMapper::toDto)
            .toList();
        return ResponseEntity.ok(accounts);
    }
}
