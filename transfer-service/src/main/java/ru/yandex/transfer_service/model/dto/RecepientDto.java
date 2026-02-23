package ru.yandex.transfer_service.model.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecepientDto(

    @NotNull
    UUID id,

    @Positive
    BigDecimal amount
) {

}