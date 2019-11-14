package com.monese.task.mapper;

import com.monese.task.dto.AccountDTO;
import com.monese.task.entity.AccountEntity;
import org.mapstruct.Mapper;

@Mapper
public interface AccountMapper {

  AccountDTO entityToDto(AccountEntity accountEntity);

  AccountEntity dtoToEntity(AccountDTO accountDTO);
}
