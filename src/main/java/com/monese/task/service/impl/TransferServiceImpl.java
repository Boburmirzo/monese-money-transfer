package com.monese.task.service.impl;

import com.monese.task.dao.AccountDAO;
import com.monese.task.dao.TransactionDAO;
import com.monese.task.dto.TransferDTO;
import com.monese.task.entity.AccountEntity;
import com.monese.task.entity.TransactionEntity;
import com.monese.task.exception.FailureMessage;
import com.monese.task.exception.FailureResponse;
import com.monese.task.exception.FailureStatusCode;
import com.monese.task.exception.RuntimeException;
import com.monese.task.mapper.TransactionMapper;
import com.monese.task.service.TransferService;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.StaleStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferServiceImpl implements TransferService {
  private static final Logger LOGGER = LoggerFactory.getLogger(TransferServiceImpl.class);

  private AccountDAO accountDao;
  private TransactionDAO transactionDAO;
  private TransactionMapper transactionMapper;

  public TransferServiceImpl(AccountDAO accountDAO,
                             TransactionDAO transactionDAO,
                             TransactionMapper transactionMapper) {
    this.accountDao = accountDAO;
    this.transactionDAO = transactionDAO;
    this.transactionMapper = transactionMapper;
  }

  @Override
  public TransferDTO transfer(TransferDTO transferDTO) throws RuntimeException {
    BigDecimal transferAmount = transferDTO.getAmount();

    AccountEntity fromAccount = accountDao.findById(transferDTO.getFromAccountId());
    AccountEntity toAccount = accountDao.findById(transferDTO.getToAccountId());

    validateTransferRequest(fromAccount, toAccount, transferAmount);

    TransactionEntity transaction = makeTransfer(fromAccount, toAccount, transferDTO);

    LOGGER.info("Transaction Successful. Transaction Details : " + transaction.toString());
    return transactionMapper.entityToDto(transaction);
  }

  @Override
  public List<TransferDTO> findAll() {
    return transactionMapper.entityToDtos(transactionDAO.findAll());
  }

  private void validateTransferRequest(AccountEntity fromAccount,
                                       AccountEntity toAccount,
                                       BigDecimal transferAmount) throws RuntimeException {
    if (fromAccount == null) {
      FailureResponse failureResponse = getFailureResponseForNoSourceAccountException();
      throw new RuntimeException(failureResponse);
    }
    if (toAccount == null) {
      FailureResponse failureResponse = getFailureResponseForNoDestinationAccountException();
      throw new RuntimeException(failureResponse);
    }
    if (!userHasSufficientBalance(fromAccount.getBalance(), transferAmount)) {
      FailureResponse failureResponse = getFailureResponseForNotSufficientBalance();
      throw new RuntimeException(failureResponse);
    }
  }

  private TransactionEntity makeTransfer(AccountEntity fromAccount,
                                         AccountEntity toAccount,
                                         TransferDTO transferDTO) throws RuntimeException {
    BigDecimal transferAmount = transferDTO.getAmount();
    debitAccount(fromAccount, transferAmount);
    creditAccount(toAccount, transferAmount);

    TransactionEntity transaction = new TransactionEntity(fromAccount.getId(),
        toAccount.getId(),
        transferAmount);
    return transactionDAO.update(transaction);
  }

  private boolean userHasSufficientBalance(BigDecimal userBalance,
                                           BigDecimal amountToTransfer) {
    return (userBalance.subtract(amountToTransfer).compareTo(BigDecimal.ZERO) >= 0);
  }

  private void debitAccount(AccountEntity account,
                            BigDecimal debitAmount) throws RuntimeException {
    try {
      BigDecimal newBalance = account.getBalance().subtract(debitAmount);
      account.setBalance(newBalance);
      accountDao.update(account);
    } catch (StaleStateException ex) {
      FailureResponse failureResponse = getFailureResponseForAccountAleadyUpdated();
      throw new RuntimeException(failureResponse);
    }
  }

  private void creditAccount(AccountEntity account,
                             BigDecimal creditAmount) {
    BigDecimal newBalance = account.getBalance().add(creditAmount);
    account.setBalance(newBalance);

    accountDao.update(account);
  }

  private FailureResponse getFailureResponseForNoSourceAccountException() {
    String failureMessage = FailureMessage.SOURCE_ACCOUNT_NOT_FOUND.message();
    int statusCode = FailureStatusCode.SOURCE_ACCOUNT_NOT_FOUND.statusCode();
    return new FailureResponse(statusCode, failureMessage);
  }

  private FailureResponse getFailureResponseForNoDestinationAccountException() {
    String failureMessage = FailureMessage.DESTINATION_ACCOUNT_NOT_FOUND.message();
    int statusCode = FailureStatusCode.DESTINATION_ACCOUNT_NOT_FOUND.statusCode();
    return new FailureResponse(statusCode, failureMessage);
  }

  private FailureResponse getFailureResponseForNotSufficientBalance() {
    String failureMessage = FailureMessage.NOT_ENOUGH_BALANCE.message();
    int statusCode = FailureStatusCode.NOT_ENOUGH_BALANCE.statusCode();
    return new FailureResponse(statusCode, failureMessage);
  }

  private FailureResponse getFailureResponseForAccountAleadyUpdated() {
    String failureMessage = FailureMessage.ACCOUNT_ALREADY_UPDATED.message();
    int statusCode = FailureStatusCode.ACCOUNT_ALREADY_UPDATED.statusCode();
    return new FailureResponse(statusCode, failureMessage);
  }
}
