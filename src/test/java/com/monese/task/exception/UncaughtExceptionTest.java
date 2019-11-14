package com.monese.task.exception;

import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UncaughtExceptionTest {

    @Test
    void toResponse_GivenAnException_ShouldReturnResponseCorrectly() {
        UncaughtException uncaughtException = new UncaughtException();
        Response response = uncaughtException.toResponse(new Exception());

        assertEquals("application", response.getMediaType().getType());
        assertEquals("json", response.getMediaType().getSubtype());
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());

    }

}