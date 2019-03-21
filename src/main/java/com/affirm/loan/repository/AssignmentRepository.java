package com.affirm.loan.repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import javafx.util.Pair;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssignmentRepository {
    private static final String[] CSV_HEADERS = {"loan_id", "facility_id"};

    List<Pair<Integer, Integer>> loanToFacility;

    public AssignmentRepository(File file) throws IOException {
        this.loanToFacility = new ArrayList<>();
        CSVReader reader = new CSVReaderBuilder(new FileReader(file))
                .withSkipLines(1)
                .build();
        String[] line;
        while ((line = reader.readNext()) != null) {
            loanToFacility.add(new Pair<>(
                    Integer.parseInt(line[0]),
                    Integer.parseInt(line[1])
            ));
        }
        reader.close();
    }

    public AssignmentRepository() {
        this.loanToFacility = new ArrayList<>();
    }


    public void add(int loanId, int facilityId) {
        loanToFacility.add(new Pair<>(loanId, facilityId));
    }

    public Integer get(int loanId) {
        for (Pair<Integer, Integer> pair : loanToFacility) {
            if (pair.getKey() == loanId) {
                return pair.getValue();
            }
        }
        return null;
    }

    public void persist(File file) throws IOException {
        CSVWriter writer = new CSVWriter(
                new FileWriter(file),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER
        );
        writer.writeNext(CSV_HEADERS);

        int totalAssigned = 0;
        for (Pair<Integer, Integer> pair : loanToFacility) {
            totalAssigned += pair.getValue() < 0 ? 0 : 1;
            String[] line = {
                    pair.getKey().toString(),
                    pair.getValue() < 0 ? "" : pair.getValue().toString()
            };
            writer.writeNext(line);
        }
        System.out.println(String.format("Total assigned %d out of %d, details in %s",
                totalAssigned, loanToFacility.size(), file.getPath()));

        writer.close();
    }
}
