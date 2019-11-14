package com.monese.task.dao;

import com.monese.task.entity.AccountEntity;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Account DAO
 */
public class AccountDAO extends AbstractDAO<AccountEntity> {
  private static final Logger LOGGER = LoggerFactory.getLogger(AccountDAO.class);

  /**
   * Account DAO constructor
   *
   * @param sessionFactory Hibernate session factory
   */
  public AccountDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * Update account data
   *
   * @param accountEntity account should be updated
   * @return updated account
   */
  public AccountEntity update(AccountEntity accountEntity) {
    LOGGER.debug("Persisting Account : " + accountEntity.toString());
    return persist(accountEntity);
  }

  /**
   * Find account by its id
   *
   * @param id id of an account
   * @return founded account by id
   */
  public AccountEntity findById(long id) {
    LOGGER.debug("Fetching account info for id : " + id);
    return currentSession().get(AccountEntity.class, id);
  }

}
