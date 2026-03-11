package ru.yandex.account_service.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import ru.yandex.account_service.model.entity.AccountEntity;

@Getter
public class CashServiceException extends ApiServiceException {

    private AccountEntity account;

    public CashServiceException(AccountEntity account, HttpStatus httpStatus, String message) {
        super(httpStatus, message);
        this.account = account;
    }
}
