package com.monese.task.integrationtest;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import com.monese.task.App;
import com.monese.task.AppConfiguration;
import com.monese.task.dto.AccountDTO;
import com.monese.task.dto.TransferDTO;
import com.monese.task.exception.FailureStatusCode;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import org.junit.jupiter.api.Disabled;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@Disabled
public class TransferControllerIntegrationTest {

    private static Client testClient;
    private static String baseURL;
    private static String transferResourcePath;
    private static String accountResourcePath;

    @Rule
    public final DropwizardAppRule<AppConfiguration> appRule = new DropwizardAppRule<>(App.class,
            ResourceHelpers.resourceFilePath("config_test.yml"));

    @Before
    public void setup() {
        testClient = new JerseyClientBuilder(appRule.getEnvironment()).build("test client");
        baseURL = String.format("http://localhost:%d/", appRule.getLocalPort());
        transferResourcePath = baseURL + "transaction/";
        accountResourcePath = baseURL + "account/";
    }


    @Test
    public void givenValidTransferRequest_ShouldReturnCorrectResponse() {
        AccountDTO testFromAccountToBeCreated = new AccountDTO("Test From Name", BigDecimal.valueOf(100));
        AccountDTO testToAccountToBeCreated = new AccountDTO("Test To Name", BigDecimal.valueOf(100));

        Response fromAccountCreateResponse = createAccount(testFromAccountToBeCreated);
        Response toAccountCreateResponse = createAccount(testToAccountToBeCreated);

        AccountDTO fromAccountCreated = fromAccountCreateResponse.readEntity(AccountDTO.class);
        AccountDTO toAccountCreated = toAccountCreateResponse.readEntity(AccountDTO.class);

        TransferDTO transferRequest = new TransferDTO(fromAccountCreated.getId(),
                toAccountCreated.getId(), BigDecimal.valueOf(50));

        Response transferResponse = makeTransfer(transferRequest);
        assertThat(transferResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        Response updatedFromAccountResponse = getAccount(fromAccountCreated.getId());
        Response updatedToAccountResponse = getAccount(toAccountCreated.getId());

        AccountDTO fromAccountUpdated = updatedFromAccountResponse.readEntity(AccountDTO.class);
        AccountDTO toAccountUpdated = updatedToAccountResponse.readEntity(AccountDTO.class);

        BigDecimal expectedFromAccountBalance = testFromAccountToBeCreated.getBalance().subtract(transferRequest.getAmount());
        BigDecimal expectedToAccountBalance = testToAccountToBeCreated.getBalance().add(transferRequest.getAmount());

        assertEquals(0, expectedFromAccountBalance.compareTo(fromAccountUpdated.getBalance()));
        assertEquals(0, expectedToAccountBalance.compareTo(toAccountUpdated.getBalance()));
    }

    @Test
    public void givenNonExistingFromAccount_ShouldReturnCorrectResponse() {
        AccountDTO testToAccountToBeCreated = new AccountDTO("Test To Name", BigDecimal.valueOf(100));

        Response toAccountCreateResponse = createAccount(testToAccountToBeCreated);

        AccountDTO toAccountCreated = toAccountCreateResponse.readEntity(AccountDTO.class);

        TransferDTO transferRequest = new TransferDTO(13123,
                toAccountCreated.getId(), BigDecimal.valueOf(50));

        Response transferResponse = makeTransfer(transferRequest);
        assertThat(transferResponse.getStatus()).isEqualTo(FailureStatusCode.SOURCE_ACCOUNT_NOT_FOUND.statusCode());

    }

    @Test
    public void givenNonExistingToAccount_ShouldReturnCorrectResponse() {
        AccountDTO testFromAccountToBeCreated = new AccountDTO("Test From Name", BigDecimal.valueOf(100));

        Response fromAccountCreateResponse = createAccount(testFromAccountToBeCreated);

        AccountDTO fromAccountCreated = fromAccountCreateResponse.readEntity(AccountDTO.class);

        TransferDTO transferRequest = new TransferDTO(fromAccountCreated.getId(), 1231231, BigDecimal.valueOf(50));

        Response transferResponse = makeTransfer(transferRequest);
        assertThat(transferResponse.getStatus()).isEqualTo(FailureStatusCode.DESTINATION_ACCOUNT_NOT_FOUND.statusCode());

    }

    @Test
    public void givenFromAccountWithNonEnoughBalance_ShouldReturnCorrectResponse() {
        AccountDTO testFromAccountToBeCreated = new AccountDTO("Test From Name", BigDecimal.valueOf(100));
        AccountDTO testToAccountToBeCreated = new AccountDTO("Test To Name", BigDecimal.valueOf(100));

        Response fromAccountCreateResponse = createAccount(testFromAccountToBeCreated);
        Response toAccountCreateResponse = createAccount(testToAccountToBeCreated);

        AccountDTO fromAccountCreated = fromAccountCreateResponse.readEntity(AccountDTO.class);
        AccountDTO toAccountCreated = toAccountCreateResponse.readEntity(AccountDTO.class);

        TransferDTO transferRequest = new TransferDTO(fromAccountCreated.getId(),
                toAccountCreated.getId(), BigDecimal.valueOf(150));

        Response transferResponse = makeTransfer(transferRequest);
        assertThat(transferResponse.getStatus()).isEqualTo(FailureStatusCode.NOT_ENOUGH_BALANCE.statusCode());

    }


    private Response makeTransfer(TransferDTO transferDTO) {
        return testClient.target(
                transferResourcePath)
                .request()
                .post(Entity.json(transferDTO));
    }

    private Response createAccount(AccountDTO accountDTO) {
        return testClient.target(
                accountResourcePath)
                .request()
                .post(Entity.json(accountDTO));
    }

    private Response getAccount(long accountId) {
        return testClient.target(
                accountResourcePath + accountId)
                .request()
                .get();
    }
}
