package com.monese.task.controller;

import com.monese.task.dto.AccountDTO;
import com.monese.task.exception.RuntimeException;
import com.monese.task.service.AccountService;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {
  private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @Path("/{accountId}")
  @GET
  @UnitOfWork
  public AccountDTO getAccount(@UnwrapValidatedValue @NotNull @Min(0) @PathParam("accountId") LongParam accountId) throws RuntimeException {
    LOGGER.info("Received account fetch request for account id: {}", accountId);
    return accountService.get(accountId.get());
  }

  @POST
  @UnitOfWork
  @Consumes(MediaType.APPLICATION_JSON)
  public AccountDTO createAccount(@Valid AccountDTO account) {
    LOGGER.info("Received account creation request: {}", account.toString());
    return accountService.create(account);
  }
}
