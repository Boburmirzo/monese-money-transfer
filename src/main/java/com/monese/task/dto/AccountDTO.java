package com.monese.task.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDTO {

  @JsonProperty
  private long id;

  @NotNull
  @NotEmpty
  @JsonProperty
  private String name;

  @NotNull
  @JsonProperty
  @DecimalMin(value = "0.0")
  private BigDecimal balance;


  public AccountDTO() {
  }

  public AccountDTO(String name,
                    BigDecimal balance) {
    this.name = name;
    this.balance = balance;
  }

  public AccountDTO(long id, String name,
                    BigDecimal balance) {
    this.id = id;
    this.name = name;
    this.balance = balance;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  @Override
  public String toString() {
    return "AccountDTO{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", balance=" + balance +
        '}';
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) {
          return true;
      }
      if (!(o instanceof AccountDTO)) {
          return false;
      }
    AccountDTO that = (AccountDTO) o;
    return getId() == that.getId() &&
        Objects.equals(getName(), that.getName()) &&
        getBalance().compareTo(that.getBalance()) == 0;
  }

}
