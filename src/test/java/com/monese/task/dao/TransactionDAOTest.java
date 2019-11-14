package com.monese.task.dao;

import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import com.monese.task.entity.TransactionEntity;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.failBecauseExceptionWasNotThrown;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(DropwizardExtensionsSupport.class)
class TransactionDAOTest {
    private final DAOTestExtension daoTestExtension = DAOTestExtension.newBuilder().addEntityClass(TransactionEntity.class).build();

    private TransactionDAO transactionDAO = new TransactionDAO(daoTestExtension.getSessionFactory());

    @Test
    void extensionCreatedSessionFactory() {
        final SessionFactory sessionFactory = daoTestExtension.getSessionFactory();

        assertThat(sessionFactory).isNotNull();
    }

    @Test
    void upsert_ShouldReturnNewlyCreatedTransactionEntity() {
        TransactionEntity testTransactionEntityToBeCreated = getTestTransactionEntity();

        TransactionEntity createdTransaction = daoTestExtension.inTransaction(() ->
                transactionDAO.update(testTransactionEntityToBeCreated));

        assertThat(createdTransaction).isEqualToComparingFieldByField(testTransactionEntityToBeCreated);
    }

    private TransactionEntity getTestTransactionEntity() {
        long fromAccountId = 1L;
        long toAccountId = 2L;
        BigDecimal testAmount = BigDecimal.valueOf(100.01);
        return new TransactionEntity(fromAccountId, toAccountId, testAmount);
    }

    @Test
    void upsert_ShouldUpdateTransactionEntityCorrectly() {
        TransactionEntity testTransactionEntityToBeCreated = getTestTransactionEntity();

        TransactionEntity createdTransaction = daoTestExtension.inTransaction(() ->
                transactionDAO.update(testTransactionEntityToBeCreated));

        long newFromAccountId = 3L;
        long newToAccountId = 2L;
        BigDecimal newAmount = BigDecimal.valueOf(100.02);

        createdTransaction.setToAccountId(newToAccountId);
        createdTransaction.setFromAccountId(newFromAccountId);
        createdTransaction.setAmount(newAmount);

        TransactionEntity updatedTransactionEntity = daoTestExtension.inTransaction(() ->
                transactionDAO.update(createdTransaction));

        assertThat(updatedTransactionEntity).isEqualToComparingFieldByField(createdTransaction);
    }

    @Test
    void upsert_IfExceptionOccurred_RollBacksSuccessfully() {
        long fromAccountId = 1L;
        long toAccountId = 2L;
        BigDecimal testAmount = BigDecimal.valueOf(100.01);
        TransactionEntity testTransactionEntityToBeCreated = getTestTransactionEntity();

        daoTestExtension.inTransaction(() -> transactionDAO.update(testTransactionEntityToBeCreated));

        testTransactionEntityToBeCreated.setAmount(BigDecimal.valueOf(300.00));
        try {
            daoTestExtension.inTransaction(() -> {
                transactionDAO.update(testTransactionEntityToBeCreated);
                transactionDAO.update(null);
            });
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException ignoredException) {
            final TransactionEntity sameTransactionEntity = transactionDAO.findById(testTransactionEntityToBeCreated.getId());
            assertThat(sameTransactionEntity.getFromAccountId()).isEqualTo(fromAccountId);
            assertThat(sameTransactionEntity.getToAccountId()).isEqualTo(toAccountId);
            assertThat(sameTransactionEntity.getAmount()).isEqualByComparingTo(testAmount);
        }
    }

    @Test
    void findById_GivenValidTransactionId_ReturnsCorrectTransactionEntity() {
        TransactionEntity testTransactionEntityToBeCreated = getTestTransactionEntity();

        daoTestExtension.inTransaction(() -> transactionDAO.update(testTransactionEntityToBeCreated));

        TransactionEntity createdTransaction = daoTestExtension.inTransaction(() -> transactionDAO.findById(testTransactionEntityToBeCreated.getId()));

        assertThat(createdTransaction).isEqualToComparingFieldByField(testTransactionEntityToBeCreated);
    }

    @Test
    void findById_GivenInvalidTransactionId_ReturnsNull() {
        TransactionEntity testTransactionEntityToBeCreated = getTestTransactionEntity();

        daoTestExtension.inTransaction(() -> transactionDAO.update(testTransactionEntityToBeCreated));

        TransactionEntity createdTransaction = daoTestExtension.inTransaction(() -> transactionDAO.findById(10));

        assertNull(createdTransaction);
    }

}