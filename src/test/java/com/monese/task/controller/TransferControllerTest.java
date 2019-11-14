package com.monese.task.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import com.monese.task.dto.TransferDTO;
import com.monese.task.exception.FailureMessage;
import com.monese.task.exception.FailureResponse;
import com.monese.task.exception.FailureStatusCode;
import com.monese.task.exception.RuntimeException;
import com.monese.task.exception.UncaughtException;
import com.monese.task.service.TransferService;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import java.math.BigDecimal;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
class TransferControllerTest {
  private static final TransferService transferService = mock(TransferService.class);
  private static final ResourceExtension RESOURCES = ResourceExtension.builder()
      .addResource(new TransferController(transferService))
      .addProvider(new RuntimeException())
      .build();


  @AfterEach
  void tearDown() {
    reset(transferService);
  }

  @Test
  void getAccount_GivenValidTransferRequest_ShouldReturnCorrectResponse() throws RuntimeException {
    TransferDTO testTransferDTO = getTestTransferDTO();

    when(transferService.transfer(any(TransferDTO.class))).thenReturn(testTransferDTO);

    final Response response = RESOURCES.target("/transaction/")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(testTransferDTO, MediaType.APPLICATION_JSON_TYPE));

    assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    assertThat(response.readEntity(TransferDTO.class)).isEqualToComparingFieldByField(testTransferDTO);
  }

  @Test
  void getAccount_IfUncaughtExceptionOccurredDuringTransfer_ShouldRespondWithCorrectStatusCode() throws RuntimeException {
    TransferDTO testTransferDTO = getTestTransferDTO();
    when(transferService.transfer(any(TransferDTO.class))).thenThrow(UncaughtException.class);

    final Response response = RESOURCES.target("/transaction/")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(testTransferDTO, MediaType.APPLICATION_JSON_TYPE));

    assertThat(response.getStatusInfo()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR);
  }

  @Test
  void getAccount_IfAnyOfTheRuntimeExceptionOccurredDuringTransfer_ShouldRespondWithCorrectStatusCode() throws RuntimeException {
    TransferDTO testTransferDTO = getTestTransferDTO();

    FailureResponse failureResponse = new FailureResponse(FailureStatusCode.SOURCE_ACCOUNT_NOT_FOUND.statusCode(),
        FailureMessage.SOURCE_ACCOUNT_NOT_FOUND.message());

    RuntimeException sourceAccountNotFoundException = new RuntimeException(failureResponse);

    when(transferService.transfer(any(TransferDTO.class))).thenThrow(sourceAccountNotFoundException);

    final Response response = RESOURCES.target("/transaction/")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(testTransferDTO, MediaType.APPLICATION_JSON_TYPE));

    assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(FailureStatusCode.SOURCE_ACCOUNT_NOT_FOUND.statusCode());

  }


  private TransferDTO getTestTransferDTO() {
    long fromAccountId = 1;
    long toAccountId = 2;
    BigDecimal amount = BigDecimal.valueOf(50);

    return new TransferDTO(fromAccountId, toAccountId, amount);
  }

}