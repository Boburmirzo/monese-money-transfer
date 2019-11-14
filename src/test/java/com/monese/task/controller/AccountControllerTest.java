package com.monese.task.controller;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import com.monese.task.dto.AccountDTO;
import com.monese.task.exception.FailureMessage;
import com.monese.task.exception.FailureResponse;
import com.monese.task.exception.FailureStatusCode;
import com.monese.task.exception.RuntimeException;
import com.monese.task.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class AccountControllerTest {

    private static final AccountService accountService = mock(AccountService.class);
    private static final ResourceExtension RESOURCES = ResourceExtension.builder()
            .addResource(new AccountController(accountService))
            .addProvider(new RuntimeException())
            .build();


    @AfterEach
    void tearDown() {
        reset(accountService);
    }

    @Test
    void getAccount_GivenValidExistingAccountId_ShouldReturnCorrectResponse() throws RuntimeException {
        AccountDTO testAccountDTO = getTestAccountDTO();
        long testAccountId = testAccountDTO.getId();

        when(accountService.get(testAccountId)).thenReturn(testAccountDTO);

        final Response response = RESOURCES.target("/account/" + testAccountId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        assertThat(response.readEntity(AccountDTO.class)).isEqualToComparingFieldByField(testAccountDTO);
    }

    @Test
    void getAccount_GivenNonExistingAccountId_ShouldRespondWithCorrectStatusCode() throws RuntimeException {
        AccountDTO testAccountDTO = getTestAccountDTO();
        long testAccountId = testAccountDTO.getId();

        FailureResponse failureResponse = new FailureResponse(FailureStatusCode.ACCOUNT_NOT_FOUND.statusCode(),
                FailureMessage.ACCOUNT_NOT_FOUND.message());

        RuntimeException accountNotFoundException = new RuntimeException(failureResponse);

        when(accountService.get(testAccountId)).thenThrow(accountNotFoundException);

        final Response response = RESOURCES.target("/account/" + testAccountId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(FailureStatusCode.ACCOUNT_NOT_FOUND.statusCode());
    }

    @Test
    void createAccount_GivenValidAccountDTO_ShouldReturnProperAccountCreationResponse() {
        AccountDTO testAccountDTO = getTestAccountDTO();

        AccountDTO accountCreated = new AccountDTO();
        accountCreated.setId(123);
        accountCreated.setName(testAccountDTO.getName());
        accountCreated.setBalance(testAccountDTO.getBalance());

        when(accountService.create(any(AccountDTO.class))).thenReturn(accountCreated);

        final Response response = RESOURCES.target("/account/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(testAccountDTO, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        assertThat(response.readEntity(AccountDTO.class)).isEqualToComparingFieldByField(accountCreated);
    }

    @Test
    void getAccount_IfExceptionOccurredDuringAccountCreation_ShouldRespondWithCorrectStatusCode() {
        AccountDTO testAccountDTO = getTestAccountDTO();

        when(accountService.create(any(AccountDTO.class))).thenThrow(Exception.class);

        final Response response = RESOURCES.target("/account/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(testAccountDTO, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR);
    }

    private AccountDTO getTestAccountDTO() {
        long testAccountId = 123;
        String testAccountName = "Some Account Name";
        BigDecimal testAmount = BigDecimal.valueOf(100.00);

        return new AccountDTO(testAccountId, testAccountName, testAmount);
    }
}