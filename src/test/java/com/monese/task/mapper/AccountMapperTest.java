package com.monese.task.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.monese.task.dto.AccountDTO;
import com.monese.task.entity.AccountEntity;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class AccountMapperTest {

  private AccountMapper accountMapper;

  @BeforeEach
  void setup() {
    accountMapper = Mappers.getMapper(AccountMapper.class);
  }

  @Test
  void entityToDto_GivenValidAccountEntity_CorrectlyMapsToAccountDTO() {
    AccountEntity testAccountEntity = new AccountEntity("Some name", BigDecimal.valueOf(100.00));
    AccountDTO mappedAccountDTO = accountMapper.entityToDto(testAccountEntity);

    assertEquals(testAccountEntity.getId(), mappedAccountDTO.getId());
    assertEquals(testAccountEntity.getName(), mappedAccountDTO.getName());
    assertEquals(testAccountEntity.getBalance(), mappedAccountDTO.getBalance());
  }

  @Test
  void entityToDto_GivenNullEntity_ShouldReturnNull() {
    AccountDTO mappedAccountDTO = accountMapper.entityToDto(null);

    assertNull(mappedAccountDTO);
  }

  @Test
  void dtoToEntity_GivenValidDTO_CorrectlyMapsToEntity() {
    AccountDTO testAccountDto = new AccountDTO("Some name", BigDecimal.valueOf(100.00));
    AccountEntity mappedAccountEntity = accountMapper.dtoToEntity(testAccountDto);

    assertEquals(testAccountDto.getName(), mappedAccountEntity.getName());
    assertEquals(testAccountDto.getBalance(), mappedAccountEntity.getBalance());
  }

  @Test
  void dtoToEntity_GivenNullDTO_ReturnsNull() {
    AccountEntity mappedAccountEntity = accountMapper.dtoToEntity(null);

    assertNull(mappedAccountEntity);
  }
}