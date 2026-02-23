package ru.yandex.cash_service.client;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


import ru.account.api.BalanceControllerApi;
import ru.account.client.ApiClient;
import ru.account.model.AccountDto;
import ru.account.model.CashDto;
import ru.yandex.cash_service.model.dto.Cash;

@Component
public class CashClient {

    private final String accountBaseUrl = "http://account-service";
    private final BalanceControllerApi balanceApi;

    public CashClient(RestClient restClient) {
        this.balanceApi = new BalanceControllerApi(new ApiClient(restClient).setBasePath(accountBaseUrl));
    }

    public AccountDto cash(UUID accountId, Cash dto) {
        CashDto cashDto = new CashDto()
            .action(dto.action())
            .amount(dto.amount())
            .accountId(accountId);
        return balanceApi.cash(cashDto);
    }
}
