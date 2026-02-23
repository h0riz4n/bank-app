package ru.yandex.account_service.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.yandex.account_service.mapper.AccountMapper;
import ru.yandex.account_service.model.dto.AccountDto;
import ru.yandex.account_service.model.dto.CashDto;
import ru.yandex.account_service.model.dto.TransferDto;
import ru.yandex.account_service.service.AccountService;

@Validated
@RestController
@RequestMapping(path = "/api/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @PutMapping(path = "/cash", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> cash(@RequestBody @Valid CashDto dto) {
        return ResponseEntity.ok(accountMapper.toDto(accountService.cash(dto.accountId(), dto.action(), dto.amount())));
    }

    @PutMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferDto dto) {
        accountService.transfer(dto.senderId(), dto.recipientId(), dto.amount());
        return ResponseEntity.noContent().build();
    }
}
