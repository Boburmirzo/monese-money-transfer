package com.monese.task.service;

import com.monese.task.dto.AccountDTO;
import com.monese.task.exception.RuntimeException;

/**
 * Account service
 */
public interface AccountService {
  /**
   * Create new account with request
   *
   * @param account Account data transfer object
   * @return created account
   */
  AccountDTO create(AccountDTO account);

  /***
   * Get an account by its id
   * @param accountId account id
   * @return Return founded account
   * @throws RuntimeException or throw not found exception by given id
   */
  AccountDTO get(long accountId) throws RuntimeException;
}
