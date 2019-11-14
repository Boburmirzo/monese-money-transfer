package com.monese.task.exception;

import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NonFoundExceptionTest {

    @Test
    void toResponse_GivenAnException_ShouldReturnResponseCorrectly() {
        NotFoundException notFoundException = new NotFoundException();
        Response response = notFoundException.toResponse(new javax.ws.rs.NotFoundException());

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
}