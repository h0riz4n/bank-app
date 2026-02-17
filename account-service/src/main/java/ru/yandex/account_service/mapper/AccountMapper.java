package ru.yandex.account_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

import lombok.RequiredArgsConstructor;
import ru.yandex.account_service.model.dto.AccountDto;
import ru.yandex.account_service.model.entity.AccountEntity;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
@RequiredArgsConstructor
public abstract class AccountMapper {

    public abstract AccountDto toDto(AccountEntity entity);

    public abstract AccountEntity toEntity(AccountDto dto);
}
