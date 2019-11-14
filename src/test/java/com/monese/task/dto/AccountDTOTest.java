package com.monese.task.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountDTOTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static AccountDTO testAccountDto;


    @BeforeClass
    public static void setup() {
        String testName = "Bobur Umurzokov";
        BigDecimal testAmount = BigDecimal.valueOf(100.01);
        testAccountDto = new AccountDTO(testName, testAmount);
    }

    @Test
    public void correctlySerializesToJSON() throws Exception {
        AccountDTO deserializedAccountDTOFromJson = MAPPER.readValue(fixture("fixtures/AccountDTO.json"), AccountDTO.class);
        final String expectedJsonString = MAPPER.writeValueAsString(deserializedAccountDTOFromJson);
        final String actualSerializedJsonString = MAPPER.writeValueAsString(testAccountDto);
        assertThat(actualSerializedJsonString).isEqualTo(expectedJsonString);
    }

    @Test
    public void correctlyDeserializesFromJSON() throws Exception {
        AccountDTO deserializedAccountDTOFromJson = MAPPER.readValue(fixture("fixtures/AccountDTO.json"), AccountDTO.class);
        assertThat(deserializedAccountDTOFromJson)
                .isEqualToComparingFieldByField(testAccountDto);
    }
}