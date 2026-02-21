package ru.yandex.account_service.model.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.yandex.account_service.model.enums.ECashAction;

public record CashDto(

    UUID accountId,

    @NotNull
    ECashAction action,

    @Positive
    BigDecimal amount
) {

}
