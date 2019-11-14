package com.monese.task.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.failBecauseExceptionWasNotThrown;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.monese.task.entity.AccountEntity;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import java.math.BigDecimal;
import javax.persistence.PersistenceException;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
class AccountDAOTest {
  private final DAOTestExtension daoTestExtension = DAOTestExtension.newBuilder().addEntityClass(AccountEntity.class).build();

  private AccountDAO accountDAO = new AccountDAO(daoTestExtension.getSessionFactory());

  private AccountEntity getTestAccountEntity() {
    String testAccountName = "Test 1";
    BigDecimal testBalance = BigDecimal.valueOf(100.00);
    return new AccountEntity(testAccountName, testBalance);
  }

  @Test
  void extensionCreatedSessionFactory_isNotNull() {
    final SessionFactory sessionFactory = daoTestExtension.getSessionFactory();

    assertThat(sessionFactory).isNotNull();
  }

  @Test
  void upsert_ShouldReturnNewlyCreatedAccountEntity() {
    AccountEntity testAccountToBeCreated = getTestAccountEntity();

    AccountEntity createdAccount = daoTestExtension.inTransaction(() ->
        accountDAO.update(testAccountToBeCreated));

    assertThat(createdAccount).isEqualToComparingFieldByField(testAccountToBeCreated);
  }

  @Test
  void upsert_ShouldUpdateAccountEntityCorrectly() {
    AccountEntity accountToBeCreated = getTestAccountEntity();

    AccountEntity createdAccountEntity = daoTestExtension.inTransaction(() ->
        accountDAO.update(accountToBeCreated));

    String testNewAccountName = "Some Other Name";
    BigDecimal testNewAmount = BigDecimal.valueOf(300.00);

    createdAccountEntity.setName(testNewAccountName);
    createdAccountEntity.setBalance(testNewAmount);

    AccountEntity updatedAccountEntity = daoTestExtension.inTransaction(() ->
        accountDAO.update(createdAccountEntity));

    assertThat(updatedAccountEntity).isEqualToComparingFieldByField(createdAccountEntity);
  }

  @Test
  void upsert_IfExceptionOccurred_RollBacksSuccessfully() {
    String testAccountName = "Test 1";
    BigDecimal testBalance = BigDecimal.valueOf(100.00);
    AccountEntity accountToBeCreated = new AccountEntity(testAccountName, testBalance);

    daoTestExtension.inTransaction(() -> accountDAO.update(accountToBeCreated));

    accountToBeCreated.setBalance(BigDecimal.valueOf(300.00));
    try {
      daoTestExtension.inTransaction(() -> {
        accountDAO.update(accountToBeCreated);
        accountDAO.update(new AccountEntity(null, null));
      });
      failBecauseExceptionWasNotThrown(PersistenceException.class);
    } catch (PersistenceException ignoredException) {
      final AccountEntity sameAccountEntity = accountDAO.findById(accountToBeCreated.getId());
      assertThat(sameAccountEntity.getName()).isEqualTo(testAccountName);
      assertThat(sameAccountEntity.getBalance()).isEqualByComparingTo(testBalance);
    }
  }

  @Test
  void findById_GivenValidAccountId_ReturnsCorrectAccount() {
    final AccountEntity testAccountToBeCreated = getTestAccountEntity();

    daoTestExtension.inTransaction(() -> accountDAO.update(testAccountToBeCreated));

    AccountEntity createdAccount = daoTestExtension.inTransaction(() -> accountDAO.findById(testAccountToBeCreated.getId()));

    assertThat(createdAccount).isEqualToComparingFieldByField(testAccountToBeCreated);
  }

  @Test
  void findById_GivenInvalidAccountId_ReturnsNull() {
    String testAccountName = "Test 3";
    BigDecimal testBalance = BigDecimal.valueOf(200.00);
    final AccountEntity testAccountToBeCreated = new AccountEntity(testAccountName, testBalance);

    daoTestExtension.inTransaction(() -> accountDAO.update(testAccountToBeCreated));

    AccountEntity createdAccount = daoTestExtension.inTransaction(() -> accountDAO.findById(10));

    assertNull(createdAccount);
  }

}