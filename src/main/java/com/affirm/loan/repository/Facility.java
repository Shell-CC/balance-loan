package com.affirm.loan.repository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Facility {
    private final int id;
    private final int bankId;
    private BigDecimal amount;
    private final BigDecimal interestRate;

    private BigDecimal maxDefaultLikelihood;
    private final Set<String> bannedStates = new HashSet<>(); // TODO: Create ENUM

    public Facility(int id, int bankId, BigDecimal amount, BigDecimal interestRate) {
        this.id = id;
        this.bankId = bankId;
        this.amount = amount;
        this.interestRate = interestRate;
    }

    public void setMaxDefaultLikelihood(BigDecimal maxDefaultLikelihood) {
        if (this.maxDefaultLikelihood != null) {
            System.out.println("WARN: duplicate maxDefaultLikelihood for facility " + id);
            this.maxDefaultLikelihood = this.maxDefaultLikelihood.min(maxDefaultLikelihood);
        } else {
            this.maxDefaultLikelihood = maxDefaultLikelihood;
        }
    }

    public void addBannedState(String state) {
        bannedStates.add(state);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public BigDecimal getMaxDefaultLikelihood() {
        return maxDefaultLikelihood;
    }

    public Set<String> getBannedStates() {
        return bannedStates;
    }

    @Override
    public String toString() {
        return "Facility{" +
                "id=" + id +
                ", bankId=" + bankId +
                ", amount=" + amount +
                ", interestRate=" + interestRate +
                ", maxDefaultLikelihood=" + maxDefaultLikelihood +
                ", bannedStates=" + bannedStates +
                '}';
    }
}
