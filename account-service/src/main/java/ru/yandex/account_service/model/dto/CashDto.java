package ru.yandex.account_service.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.yandex.account_service.model.enums.ECashAction;

public record CashDto(

    @NotNull
    ECashAction action,

    @Positive
    BigDecimal amount
) {

}
