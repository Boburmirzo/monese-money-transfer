package com.monese.task.exception;

public enum FailureMessage {
  ACCOUNT_NOT_FOUND("No account found for this id"),
  SOURCE_ACCOUNT_NOT_FOUND("Source account not found for this id"),
  DESTINATION_ACCOUNT_NOT_FOUND("Destination account not found for this id"),
  NOT_ENOUGH_BALANCE("Not enough balance for this account"),
  ACCOUNT_ALREADY_UPDATED("Your account was updated or deleted by another transaction");

  private String message;

  FailureMessage(String message) {
    this.message = message;
  }

  public String message() {
    return message;
  }
}
