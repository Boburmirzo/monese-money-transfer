package com.monese.task.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundException implements ExceptionMapper<javax.ws.rs.NotFoundException> {

  public Response toResponse(javax.ws.rs.NotFoundException exception) {
    return Response.status(Response.Status.NOT_FOUND)
        .entity("No such resource")
        .build();
  }
}