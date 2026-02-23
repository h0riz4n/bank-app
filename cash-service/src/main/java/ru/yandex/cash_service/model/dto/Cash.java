package ru.yandex.cash_service.model.dto;

import java.math.BigDecimal;

import ru.account.model.CashDto.ActionEnum;

public record Cash(

    ActionEnum action,

    BigDecimal amount
) {

}
