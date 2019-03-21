package com.affirm.loan.service;

import com.affirm.loan.repository.AssignmentRepository;
import com.affirm.loan.repository.Facility;
import com.affirm.loan.repository.FacilityRepository;
import com.affirm.loan.repository.YieldRepository;

import java.math.BigDecimal;

public class LoanService {

    private final FacilityRepository facilityRepository;
    private final AssignmentRepository assignmentRepository;
    private final YieldRepository yieldRepository;

    public LoanService(FacilityRepository facilityRepository,
                       AssignmentRepository assignmentRepository,
                       YieldRepository yieldRepository) {
        this.facilityRepository = facilityRepository;
        this.assignmentRepository = assignmentRepository;
        this.yieldRepository = yieldRepository;
    }

    public int assign(Loan loan) {
        Facility facility = facilityRepository.findCheapestWithMostAmount(
                loan.getAmount(),
                loan.getDefaultLikelihood(),
                loan.getState()
        );
        int facilityId = facility == null ? -1 : facility.getId();
        assignmentRepository.add(loan.getId(), facilityId);
        if (facilityId > 0) {
            BigDecimal newAmount = facility.getAmount().subtract(loan.getAmount());
            facility.setAmount(newAmount);
            BigDecimal expectYield = calculateExpectedYield(loan, facility);
            yieldRepository.add(facilityId, expectYield);
        }
        return facilityId;
    }

    private BigDecimal calculateExpectedYield(Loan loan, Facility facility) {
        BigDecimal expectInterest = BigDecimal.ONE
                .subtract(loan.getDefaultLikelihood())
                .multiply(loan.getInterestRate())
                .multiply(loan.getAmount());
        BigDecimal expectLoss = loan.getAmount().multiply(loan.getDefaultLikelihood());
        BigDecimal payToFacility = loan.getAmount().multiply(facility.getInterestRate());
        BigDecimal expectYield =  expectInterest.subtract(expectLoss).subtract(payToFacility);
        assert expectYield.signum() >= 0;
        return expectYield;
    }
}
