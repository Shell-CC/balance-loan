package com.affirm.loan.repository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Covenants {

    private BigDecimal maxDefaultLikelihood;
    private final Set<String> bannedStates = new HashSet<>(); // TODO: Create ENUM

    public void add(Covenants other) {
        if (other.getMaxDefaultLikelihood() != null) {
            this.setMaxDefaultLikelihood(other.getMaxDefaultLikelihood());
        }
        for (String state : other.getBannedStates()) {
            this.addBannedState(state);
        }
    }

    public void setMaxDefaultLikelihood(BigDecimal maxDefaultLikelihood) {
        if (this.maxDefaultLikelihood != null) {
            System.out.println("WARN: duplicate maxDefaultLikelihood");
            this.maxDefaultLikelihood = this.maxDefaultLikelihood.min(maxDefaultLikelihood);
        } else {
            this.maxDefaultLikelihood = maxDefaultLikelihood;
        }
    }

    public void addBannedState(String state) {
        bannedStates.add(state);
    }

    public BigDecimal getMaxDefaultLikelihood() {
        return maxDefaultLikelihood;
    }

    public Set<String> getBannedStates() {
        return bannedStates;
    }

    public boolean isSatisfied(BigDecimal defaultLikelihood,
                               String state) {
        return maxDefaultLikelihood.compareTo(defaultLikelihood) >= 0
                && !bannedStates.contains(state);
    }

    @Override
    public String toString() {
        return "Covenants{" +
                "maxDefaultLikelihood=" + maxDefaultLikelihood +
                ", bannedStates=" + bannedStates +
                '}';
    }
}
