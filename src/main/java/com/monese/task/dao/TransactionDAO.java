package com.monese.task.dao;

import com.monese.task.entity.TransactionEntity;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionDAO extends AbstractDAO<TransactionEntity> {
  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionDAO.class);

  public TransactionDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public TransactionEntity update(TransactionEntity transactionEntity) {
    LOGGER.debug("Persisting Transaction : " + transactionEntity.toString());
    return persist(transactionEntity);
  }

  public TransactionEntity findById(long id) {
    LOGGER.debug("Fetch transaction for id: " + id);
    return currentSession().get(TransactionEntity.class, id);
  }

  public List<TransactionEntity> findAll() {
      LOGGER.debug("Fetch all transactions");
      return list(namedQuery("com.monese.task.entity.TransactionEntity.findAll"));
  }

}
