package com.monese.task.exception;

public class FailureResponse {
  private int statusCode;
  private String message;

  public FailureResponse(int statusCode,
                         String message) {
    this.statusCode = statusCode;
    this.message = message;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getMessage() {
    return message;
  }

}
