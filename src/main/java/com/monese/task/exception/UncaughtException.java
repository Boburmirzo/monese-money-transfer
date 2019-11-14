package com.monese.task.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class UncaughtException extends Throwable implements ExceptionMapper<Throwable> {
  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(UncaughtException.class);

  @Override
  public Response toResponse(Throwable exception) {
    LOGGER.error(exception.getMessage());
    return Response.status(500).entity("Something bad happened. Please try again !!").type("application/json").build();
  }
}