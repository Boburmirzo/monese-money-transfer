package com.monese.task.service.impl;

import com.monese.task.dao.AccountDAO;
import com.monese.task.dto.AccountDTO;
import com.monese.task.entity.AccountEntity;
import com.monese.task.exception.FailureMessage;
import com.monese.task.exception.FailureResponse;
import com.monese.task.exception.FailureStatusCode;
import com.monese.task.exception.RuntimeException;
import com.monese.task.mapper.AccountMapper;
import com.monese.task.service.AccountService;

public class AccountServiceImpl implements AccountService {
  private AccountDAO accountDao;
  private AccountMapper accountMapper;

  public AccountServiceImpl(AccountDAO accountDao,
                            AccountMapper accountMapper) {
    this.accountDao = accountDao;
    this.accountMapper = accountMapper;
  }

  @Override
  public AccountDTO create(AccountDTO accountToBeCreated) {
    AccountEntity accountEntity = accountMapper.dtoToEntity(accountToBeCreated);
    AccountEntity createdAccount = accountDao.update(accountEntity);
    return accountMapper.entityToDto(createdAccount);
  }

  @Override
  public AccountDTO get(long accountId) throws RuntimeException {
    AccountEntity singleAccount = accountDao.findById(accountId);
    if (singleAccount == null) {
      FailureResponse failureResponse = getFailureResponseForAccountNotFoundException();
      throw new RuntimeException(failureResponse);
    }
    return accountMapper.entityToDto(singleAccount);
  }

  private FailureResponse getFailureResponseForAccountNotFoundException() {
    String failureMessage = FailureMessage.ACCOUNT_NOT_FOUND.message();
    int statusCode = FailureStatusCode.ACCOUNT_NOT_FOUND.statusCode();
    return new FailureResponse(statusCode, failureMessage);
  }


}
