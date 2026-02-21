package ru.yandex.practicum.mybankfront.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Account(

    UUID id,

    String login,

    String firstName,

    String lastName,

    BigDecimal amount,

    LocalDate birthDate,

    LocalDateTime creationDateTime,
    
    LocalDateTime updateDateTime
) {

}
