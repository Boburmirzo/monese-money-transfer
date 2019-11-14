package com.monese.task.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transaction")
@NamedQueries({
    @NamedQuery(name = "com.monese.task.entity.TransactionEntity.findAll", query = "SELECT t FROM TransactionEntity t")
})
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "fromAccountId", nullable = false)
    private long fromAccountId;

    @Column(name = "toAccountId", nullable = false)
    private long toAccountId;

    @Column(name = "amount", precision = 19, scale = 4)
    private BigDecimal amount;

    public TransactionEntity() {
    }

    public TransactionEntity(long fromAccountId, long toAccountId, BigDecimal amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public long getId() {
        return id;
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
        return "TransactionEntity{" +
                "id=" + id +
                ", fromAccountId=" + fromAccountId +
                ", toAccountId=" + toAccountId +
                ", amount=" + amount +
                '}';
    }
}
