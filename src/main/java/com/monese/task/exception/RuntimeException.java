package com.monese.task.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class RuntimeException extends Exception implements
    ExceptionMapper<RuntimeException> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeException.class);
  private static final long serialVersionUID = 1L;
  private FailureResponse failureResponse;

  public RuntimeException() {
  }

  public RuntimeException(FailureResponse failureResponse) {
    super(failureResponse.getMessage());
    this.failureResponse = failureResponse;
  }

  @Override
  public Response toResponse(RuntimeException exception) {
    LOGGER.info(exception.getFailureResponse().getMessage());
    return Response.status(exception.failureResponse.getStatusCode())
        .entity(exception.failureResponse)
        .type("application/json").build();
  }

  public FailureResponse getFailureResponse() {
    return failureResponse;
  }
}
