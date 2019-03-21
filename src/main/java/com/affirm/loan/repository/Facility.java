package com.affirm.loan.repository;

import java.math.BigDecimal;

public class Facility {
    private final int id;
    private final int bankId;
    private BigDecimal amount;
    private final BigDecimal interestRate;
    private Covenants covenants;

    public Facility(int id, int bankId, BigDecimal amount, BigDecimal interestRate) {
        this.id = id;
        this.bankId = bankId;
        this.amount = amount;
        this.interestRate = interestRate;
        this.covenants = new Covenants();
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Covenants getCovenants() {
        return covenants;
    }

    public int getId() {
        return id;
    }

    public int getBankId() {
        return bankId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    @Override
    public String toString() {
        return "Facility{" +
                "id=" + id +
                ", bankId=" + bankId +
                ", amount=" + amount +
                ", interestRate=" + interestRate +
                ", covenants=" + covenants +
                '}';
    }
}
