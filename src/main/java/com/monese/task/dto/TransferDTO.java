package com.monese.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TransferDTO {
  @JsonProperty
  @NotNull
  @Min(value = 0L, message = "From accountId must be a positive number")
  private long fromAccountId;

  @JsonProperty
  @NotNull
  @Min(value = 0L, message = "From accountId must be a positive number")
  private long toAccountId;

  @JsonProperty
  @NotNull
  @DecimalMin(value = "0.0", message = "TransactionEntity amount must be a positive value")
  private BigDecimal amount;

  public TransferDTO() {
  }

  public TransferDTO(long fromAccountId,
                     long toAccountId,
                     BigDecimal amount) {
    this.fromAccountId = fromAccountId;
    this.toAccountId = toAccountId;
    this.amount = amount;
  }

  public long getFromAccountId() {
    return fromAccountId;
  }

  public void setFromAccountId(long fromAccountId) {
    this.fromAccountId = fromAccountId;
  }

  public long getToAccountId() {
    return toAccountId;
  }

  public void setToAccountId(long toAccountId) {
    this.toAccountId = toAccountId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "TransferDTO{" +
        "fromAccountId=" + fromAccountId +
        ", toAccountId=" + toAccountId +
        ", amount=" + amount +
        '}';
  }
}
