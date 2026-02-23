package ru.yandex.practicum.mybankfront.model;

import java.math.BigDecimal;
import java.util.UUID;

public record Transfer(

    UUID id,

    BigDecimal amount
) {

}
