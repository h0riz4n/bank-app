package ru.yandex.account_service.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import lombok.RequiredArgsConstructor;
import ru.yandex.account_service.client.NotificationClient;
import ru.yandex.account_service.exception.ApiServiceException;
import ru.yandex.account_service.model.entity.AccountEntity;
import ru.yandex.account_service.model.enums.ECashAction;
import ru.yandex.account_service.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final TransactionTemplate transactionTemplate;
    private final NotificationClient notificationClient;
    private final AccountRepository accountRepo;

    public List<AccountEntity> getAll() {
        return accountRepo.findAllByIdNot(getAccountId(getCurrentAuth().getToken()));
    }

    @Transactional
    public AccountEntity getAccount() {
        var token = getCurrentAuth().getToken();
        return accountRepo.findById(getAccountId(token))
            .orElseGet(() -> accountRepo.save(createAccount(token)));
    }

    public AccountEntity update(String firstName, String lastName, LocalDate birthDate) {
        var account = transactionTemplate.execute(action -> {
            var currentAccount = loadAccount();
            currentAccount.setFirstName(firstName);
            currentAccount.setLastName(lastName);
            currentAccount.setBirthDate(birthDate);
            return accountRepo.save(currentAccount);
        });
        notificationClient.notify("Обновлён аккаунт с ID: %s".formatted(account.getId()));
        return account;
    }
    
    @Transactional
    public AccountEntity cash(ECashAction cashAction, BigDecimal amount) {
        var currentAccount = loadAccount();
        var newAmount = switch (cashAction) {
            case GET -> withdraw(currentAccount, amount);
            case PUT -> deposit(currentAccount, amount);
        };
        currentAccount.setAmount(newAmount);
        return accountRepo.save(currentAccount);
        // notificationClient.notify("Изменён баланс счёта %s. Теперь баланс составляет %s".formatted(account.getId(), account.getAmount()));
    }

    @Transactional
    public void transfer(UUID senderId, UUID recipientId, BigDecimal amount) {
        var sender = loadAccount(senderId);
        var recipient = loadAccount(recipientId);

        if (sender.getAmount().compareTo(amount) < 0) {
            throw new ApiServiceException(HttpStatus.BAD_REQUEST, "Недостаточно средств");
        }

        sender.setAmount(sender.getAmount().subtract(amount));
        recipient.setAmount(recipient.getAmount().add(amount));
        accountRepo.saveAll(List.of(sender, recipient));
        // notificationClient.notify("Был произведён перевод на сумму %s со счёта %s на %s".formatted(amount, senderId, recipientId));
    }

    private AccountEntity createAccount(Jwt jwt) {
        return AccountEntity.builder()
            .id(getAccountId(jwt))
            .login(jwt.getClaimAsString("email"))
            .firstName(jwt.getClaimAsString("given_name"))
            .lastName(jwt.getClaimAsString("family_name"))
            .birthDate(LocalDate.parse(jwt.getClaimAsString("birth_date"), DateTimeFormatter.ofPattern("dd.MM.yyyy")))
            .amount(BigDecimal.ZERO)
            .build();
    }

    private AccountEntity loadAccount() {
        return loadAccount(getAccountId(getCurrentAuth().getToken()));
    }

    private AccountEntity loadAccount(UUID id) {
        return accountRepo.findById(id)
            .orElseThrow(() -> new ApiServiceException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

    private BigDecimal withdraw(AccountEntity account, BigDecimal amount) {
        if (account.getAmount().compareTo(amount) < 0) {
            throw new ApiServiceException(HttpStatus.BAD_REQUEST, "Недостаточно средств");
        }
        return account.getAmount().subtract(amount);
    }

    private BigDecimal deposit(AccountEntity account, BigDecimal amount) {
        return account.getAmount().add(amount);
    }

    private UUID getAccountId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    private JwtAuthenticationToken getCurrentAuth() {
        return (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    } 
}
