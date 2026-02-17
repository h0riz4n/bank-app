package ru.yandex.account_service.model.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferDto(

    @NotNull
    UUID senderId,

    @NotNull
    UUID recipientId,

    @Positive
    BigDecimal amount
) {

}
