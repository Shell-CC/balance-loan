package com.affirm.loan;

import com.affirm.loan.repository.AssignmentRepository;
import com.affirm.loan.repository.FacilityRepository;
import com.affirm.loan.repository.YieldRepository;
import com.affirm.loan.service.Loan;
import com.affirm.loan.service.LoanService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class App {

    // input
    private static final String CSV_FACILITY = "facilities.csv";
    private static final String CSV_COVENANT = "covenants.csv";
    private static final String CSV_LOAN = "loans.csv";
    static FacilityRepository facilityRepository;

    // output
    private static final String CSV_ASSIGNMENT = "assignments.csv";
    private static final String CSV_FIELD = "yields.csv";
    static AssignmentRepository assignmentRepository;
    static YieldRepository yieldRepository;

    private static void initializeContainers() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File facilityFile = new File(classLoader.getResource(CSV_FACILITY).getFile());
        File covenantFile = new File(classLoader.getResource(CSV_COVENANT).getFile());
        facilityRepository = new FacilityRepository(facilityFile, covenantFile);
        assignmentRepository = new AssignmentRepository();
        yieldRepository = new YieldRepository();
    }

    private static List<Loan> getLoanStream() throws IOException {
        List<Loan> loans = new ArrayList<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource(CSV_LOAN).getFile());
        CSVReader reader = new CSVReaderBuilder(new FileReader(file)).withSkipLines(1).build();
        String[] line;
        while ((line = reader.readNext()) != null) {
            Loan loan = new Loan(
                    Integer.parseInt(line[2]),
                    new BigDecimal(line[1]),
                    new BigDecimal(line[0]),
                    new BigDecimal(line[3]),
                    line[4]
            );
            loans.add(loan);
        }
        reader.close();

        return loans;
    }

    private static void persistOutput() throws IOException {
        File assignmentFile = new File(CSV_ASSIGNMENT);
        File yieldFile = new File(CSV_FIELD);
        assignmentRepository.persist(assignmentFile);
        yieldRepository.persist(yieldFile);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Starting service...");
        initializeContainers();

        System.out.println("Processing load stream...");
        List<Loan> loans = getLoanStream();
        LoanService loanService = new LoanService(facilityRepository, assignmentRepository, yieldRepository);
        for (Loan loan : loans) {
            loanService.assign(loan);
        }

        System.out.println("Shutting down service...");
        persistOutput();
    }
}
