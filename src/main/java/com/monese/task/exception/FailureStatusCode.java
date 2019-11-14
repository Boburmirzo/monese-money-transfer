package com.monese.task.exception;


public enum FailureStatusCode {
  ACCOUNT_NOT_FOUND(600),
  SOURCE_ACCOUNT_NOT_FOUND(601),
  DESTINATION_ACCOUNT_NOT_FOUND(602),
  NOT_ENOUGH_BALANCE(603),
  ACCOUNT_ALREADY_UPDATED(604);

  private int statusCode;

  FailureStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public int statusCode() {
    return statusCode;
  }
}
