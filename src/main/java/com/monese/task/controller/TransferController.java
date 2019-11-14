package com.monese.task.controller;

import com.monese.task.dto.TransferDTO;
import com.monese.task.exception.RuntimeException;
import com.monese.task.service.TransferService;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
public class TransferController {
  private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

  private final TransferService transferService;

  public TransferController(TransferService transferService) {
    this.transferService = transferService;
  }

  @POST
  @UnitOfWork
  @Consumes(MediaType.APPLICATION_JSON)
  public TransferDTO transfer(@Valid TransferDTO transferDTO) throws RuntimeException {
    LOGGER.info("Received transfer request: {}", transferDTO.toString());
    return transferService.transfer(transferDTO);
  }

  @GET
  @UnitOfWork
  public List<TransferDTO> getAllTransactions() {
    return transferService.findAll();
  }
}
