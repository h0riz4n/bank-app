package ru.yandex.account_service.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.account_service.model.view.AccountView;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AccountDto {

    @JsonView({ AccountView.Detail.class })
    private UUID id;

    @JsonView({ AccountView.Detail.class })
    private String login;

    @NotEmpty
    @JsonView({ AccountView.Detail.class, AccountView.UpdateProfile.class })
    private String firstName;

    @NotEmpty
    @JsonView({ AccountView.Detail.class, AccountView.UpdateProfile.class })
    private String lastName;

    @Past
    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonView({ AccountView.Detail.class, AccountView.UpdateProfile.class })
    private LocalDate birthDate;

    @JsonView({ AccountView.Detail.class })
    private BigDecimal amount;

    @JsonView({ AccountView.Detail.class })
    private LocalDateTime creationDateTime;

    @JsonView({ AccountView.Detail.class })
    private LocalDateTime updateDateTime;
}
