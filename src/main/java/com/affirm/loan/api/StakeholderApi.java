package com.affirm.loan.api;

import com.affirm.loan.repository.AssignmentRepository;
import com.affirm.loan.repository.Facility;
import com.affirm.loan.repository.FacilityRepository;
import com.affirm.loan.service.Loan;
import com.affirm.loan.service.LoanService;

import java.math.BigDecimal;

public class StakeholderApi {
    private LoanService loanService;
    private AssignmentRepository assignmentRepository;
    private FacilityRepository facilityRepository;

    // REST API USING POST
    public int assignLoan(Loan loan) {
        return loanService.assign(loan);
    }

    // REST API USING GET
    public int getFundingStatus(int loanId) {
        Integer assignedId = assignmentRepository.get(loanId);
        if (assignedId == null) {
            // return 404
        }
        return assignedId;
    }

    // REST API USING GET
    public float getCapacity(int facilityId) {
        Facility facility = facilityRepository.get(facilityId);
        if (facility == null) {
            // return 404
        }
        return facility.getAmount().setScale(2, BigDecimal.ROUND_FLOOR).floatValue();
    }
}
