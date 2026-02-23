package ru.yandex.practicum.mybankfront.exception;

public class ApiServiceException extends RuntimeException {

    public ApiServiceException(String message) {
        super(message);
    }
}
