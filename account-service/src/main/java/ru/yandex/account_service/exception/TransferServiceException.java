package ru.yandex.account_service.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import ru.yandex.account_service.model.entity.AccountEntity;

@Getter
public class TransferServiceException extends ApiServiceException {

    private AccountEntity sender;
    private AccountEntity recipient;

    public TransferServiceException(AccountEntity sender, AccountEntity recipient, HttpStatus httpStatus, String message) {
        super(httpStatus, message);
        this.sender = sender;
        this.recipient = recipient;
    }
}