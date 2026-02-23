package ru.yandex.cash_service.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.account.model.AccountDto;
import ru.yandex.cash_service.model.dto.Cash;
import ru.yandex.cash_service.service.CashService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/cash", consumes = MediaType.APPLICATION_JSON_VALUE)
public class CashController {

    private final CashService cashService;

    @PutMapping
    public ResponseEntity<AccountDto> cash(@RequestBody Cash dto) {
        return ResponseEntity.ok(cashService.cash(dto));
    }
}
