package com.monese.task;

import com.monese.task.controller.AccountController;
import com.monese.task.controller.TransferController;
import com.monese.task.dao.AccountDAO;
import com.monese.task.dao.TransactionDAO;
import com.monese.task.entity.AccountEntity;
import com.monese.task.entity.TransactionEntity;
import com.monese.task.exception.NotFoundException;
import com.monese.task.exception.RuntimeException;
import com.monese.task.exception.UncaughtException;
import com.monese.task.mapper.AccountMapper;
import com.monese.task.mapper.TransactionMapper;
import com.monese.task.service.AccountService;
import com.monese.task.service.TransferService;
import com.monese.task.service.impl.AccountServiceImpl;
import com.monese.task.service.impl.TransferServiceImpl;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.SessionFactoryHealthCheck;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.mapstruct.factory.Mappers;

public class App extends Application<AppConfiguration> {

  private HibernateBundle<AppConfiguration> hibernateBundle = new HibernateBundle<AppConfiguration>(AccountEntity.class,
      TransactionEntity.class) {
    public DataSourceFactory getDataSourceFactory(AppConfiguration configuration) {
      return configuration.getDataSourceFactory();
    }
  };

  public static void main(final String[] args) throws Exception {
    new App().run(args);
  }

  @Override
  public String getName() {
    return "App";
  }

  @Override
  public void initialize(final Bootstrap<AppConfiguration> bootstrap) {
    bootstrap.addBundle(hibernateBundle);
  }

  @Override
  public void run(final AppConfiguration configuration,
                  final Environment environment) {
    final AccountDAO accountDAO = new AccountDAO(hibernateBundle.getSessionFactory());
    final TransactionDAO transactionDAO = new TransactionDAO(hibernateBundle.getSessionFactory());

    final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    final TransactionMapper transactionMapper = Mappers.getMapper(TransactionMapper.class);

    final AccountService accountService = new AccountServiceImpl(accountDAO, accountMapper);
    final TransferService transferService = new TransferServiceImpl(accountDAO, transactionDAO, transactionMapper);

    SessionFactoryHealthCheck sessionFactoryHealthCheck = new SessionFactoryHealthCheck(hibernateBundle.getSessionFactory(), "SELECT 1");
    environment.healthChecks().register("Database Health Check", sessionFactoryHealthCheck);

    environment.jersey().register(new AccountController(accountService));
    environment.jersey().register(new TransferController(transferService));
    environment.jersey().register(new RuntimeException());
    environment.jersey().register(new NotFoundException());
    environment.jersey().register(new UncaughtException());
  }

}
