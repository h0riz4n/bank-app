package ru.yandex.practicum.mybankfront.model;

import java.time.LocalDate;

public record UpdateAccount(
    
    String firstName,

    String lastName,

    LocalDate birthDate
) {

}
