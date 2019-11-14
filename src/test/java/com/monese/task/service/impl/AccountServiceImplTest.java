package com.monese.task.service.impl;

import com.monese.task.dto.AccountDTO;
import com.monese.task.entity.AccountEntity;
import com.monese.task.dao.AccountDAO;
import com.monese.task.exception.FailureMessage;
import com.monese.task.exception.FailureStatusCode;
import com.monese.task.exception.RuntimeException;
import com.monese.task.mapper.AccountMapper;
import com.monese.task.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {

    private final AccountDAO accountDao = mock(AccountDAO.class);

    private final AccountMapper accountMapper = mock(AccountMapper.class);

    private AccountService accountService;


    @BeforeEach
    void setup() {
        accountService = new AccountServiceImpl(accountDao, accountMapper);
    }

    @Test
    void create_GivenValidAccountDTO_ShouldReturnMappedDTO() {
        long testAccountId = 123;
        String testAccountName = "Some Account Name";
        BigDecimal testAmount = BigDecimal.valueOf(100.00);

        AccountDTO testAccountDTO = new AccountDTO(testAccountId, testAccountName, testAmount);

        AccountEntity testAccountEntityToBeCreated = new AccountEntity(testAccountName, testAmount);
        AccountEntity testAccountEntityCreated = new AccountEntity(testAccountName, testAmount);

        when(accountMapper.dtoToEntity(testAccountDTO)).thenReturn(testAccountEntityToBeCreated);
        when(accountDao.update(testAccountEntityToBeCreated)).thenReturn(testAccountEntityCreated);
        when(accountMapper.entityToDto(testAccountEntityCreated)).thenReturn(testAccountDTO);

        assertEquals(testAccountDTO, accountService.create(testAccountDTO));
    }

    @Test
    void get_GivenNonExistingAccountId_ShouldThrowAccountNotFoundException() {
        long testAccountId = 123;
        when(accountDao.findById(testAccountId)).thenReturn(null);

        RuntimeException accountNotFoundException = assertThrows(RuntimeException.class, () -> accountService.get(testAccountId));

        assertEquals(FailureStatusCode.ACCOUNT_NOT_FOUND.statusCode(), accountNotFoundException.getFailureResponse().getStatusCode());
        assertEquals(FailureMessage.ACCOUNT_NOT_FOUND.message(), accountNotFoundException.getFailureResponse().getMessage());
    }

    @Test
    void get_GivenExistingAccountId_ShouldReturnCorrectAccountDTO() throws RuntimeException {
        long testAccountId = 123;
        String testAccountName = "Some Account Name";
        BigDecimal testAmount = BigDecimal.valueOf(100.00);

        AccountDTO testAccountDTO = new AccountDTO(testAccountId, testAccountName, testAmount);
        AccountEntity testAccountEntity = new AccountEntity(testAccountName, testAmount);

        when(accountDao.findById(testAccountId)).thenReturn(testAccountEntity);
        when(accountMapper.entityToDto(testAccountEntity)).thenReturn(testAccountDTO);

        assertEquals(testAccountDTO, accountService.get(testAccountId));
    }
}