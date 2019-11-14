package com.monese.task.exception;

import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RuntimeExceptionTest {

    @Test
    void toResponse_GivenAnException_ShouldReturnResponseCorrectly() {
        int testStatusCode = 100;
        String testMessage = "Some Error Occurred";
        FailureResponse testFailureResponse = new FailureResponse(testStatusCode, testMessage);

        RuntimeException testExceptionMapper = new RuntimeException(testFailureResponse);

        Response response = testExceptionMapper.toResponse(testExceptionMapper);

        assertEquals("application", response.getMediaType().getType());
        assertEquals("json", response.getMediaType().getSubtype());
        assertEquals(testFailureResponse, response.getEntity());
        assertEquals(testStatusCode, response.getStatus());

    }
}