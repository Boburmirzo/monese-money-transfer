package com.monese.task.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;


public class TransferDTOTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static TransferDTO testTransferDTO;

    @BeforeClass
    public static void setup() {
        long fromAccountId = 1L;
        long toAccountId = 2L;
        BigDecimal testAmount = BigDecimal.valueOf(100.01);
        testTransferDTO = new TransferDTO(fromAccountId, toAccountId, testAmount);
    }

    @Test
    public void correctlySerializesToJSON() throws Exception {
        TransferDTO deserializedTransferDTOFromJson = MAPPER.readValue(fixture("fixtures/TransferDTO.json"), TransferDTO.class);
        final String expectedJsonString = MAPPER.writeValueAsString(deserializedTransferDTOFromJson);
        final String actualSerializedJsonString = MAPPER.writeValueAsString(testTransferDTO);
        assertThat(actualSerializedJsonString).isEqualTo(expectedJsonString);
    }

    @Test
    public void correctlyDeserializesFromJSON() throws Exception {
        TransferDTO deserializedTransferDTOFromJson = MAPPER.readValue(fixture("fixtures/TransferDTO.json"), TransferDTO.class);
        assertThat(deserializedTransferDTOFromJson)
                .isEqualToComparingFieldByField(testTransferDTO);
    }

}