package ru.yandex.transfer_service.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.yandex.transfer_service.model.dto.RecepientDto;
import ru.yandex.transfer_service.service.TransferService;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/api/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Void> transfer(@RequestBody @Valid RecepientDto recepientDto) {
        transferService.transfer(recepientDto);
        return ResponseEntity.noContent().build();
    }
}
