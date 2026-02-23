package ru.yandex.practicum.mybankfront.model;

import java.math.BigDecimal;

import ru.yandex.practicum.mybankfront.model.dto.CashAction;

public record Cash(
    
    CashAction action,
    
    BigDecimal amount
) {
}
