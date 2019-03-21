package com.affirm.loan.service;

import java.math.BigDecimal;

public class Loan {
    private final int id;
    private final BigDecimal amount;
    private final BigDecimal interestRate;
    private final BigDecimal defaultLikelihood;
    private final String State;

    public Loan(int id,
                BigDecimal amount,
                BigDecimal interestRate,
                BigDecimal defaultLikelihood,
                String state) {
        this.id = id;
        this.amount = amount;
        this.interestRate = interestRate;
        this.defaultLikelihood = defaultLikelihood;
        State = state;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public BigDecimal getDefaultLikelihood() {
        return defaultLikelihood;
    }

    public String getState() {
        return State;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", amount=" + amount +
                ", interestRate=" + interestRate +
                ", defaultLikelihood=" + defaultLikelihood +
                ", State='" + State + '\'' +
                '}';
    }
}
