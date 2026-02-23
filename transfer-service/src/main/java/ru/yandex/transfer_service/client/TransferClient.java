package ru.yandex.transfer_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


import ru.account.api.BalanceControllerApi;
import ru.account.client.ApiClient;
import ru.account.model.TransferDto;

@Component
public class TransferClient {

    private final String accountBaseUrl = "http://account-service";
    private final BalanceControllerApi balanceApi;

    public TransferClient(RestClient restClient) {
        this.balanceApi = new BalanceControllerApi(new ApiClient(restClient).setBasePath(accountBaseUrl));
    }

    public void transfer(TransferDto dto) {
        balanceApi.transfer(dto);
    }
}
