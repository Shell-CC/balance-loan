package com.affirm.loan.repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class YieldRepository {
    private static final String[] CSV_HEADERS = {"facility_id", "expected_yield"};

    Map<Integer, BigDecimal> facilityToYield;

    public YieldRepository(File file) throws IOException {
        facilityToYield = new HashMap<>();
        CSVReader reader = new CSVReaderBuilder(new FileReader(file))
                .withSkipLines(1)
                .build();
        String[] line;
        while ((line = reader.readNext()) != null) {
            facilityToYield.put(
                    Integer.parseInt(line[0]),
                    new BigDecimal(line[1])
            );
        }
        reader.close();
    }

    public YieldRepository() {
        facilityToYield = new HashMap<>();
    }

    public void add(int facilityId, BigDecimal expectedYield) {
        BigDecimal yield = facilityToYield.getOrDefault(facilityId, BigDecimal.ZERO);
        facilityToYield.put(facilityId, yield.add(expectedYield));
    }

    public BigDecimal get(int facilityId) {
        return facilityToYield.get(facilityId);
    }

    public void persist(File file) throws IOException {
        CSVWriter writer = new CSVWriter(
                new FileWriter(file),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER
        );
        writer.writeNext(CSV_HEADERS);

        BigDecimal totalYields = BigDecimal.ZERO;
        for (Map.Entry<Integer, BigDecimal> entry: facilityToYield.entrySet()) {
            totalYields = totalYields.add(entry.getValue());
            String[] line = {
                    entry.getKey().toString(),
                    entry.getValue().setScale(2, BigDecimal.ROUND_CEILING).toString()
            };
            writer.writeNext(line);
        }
        System.out.println("Total yields " + totalYields.toString() + ", details in " + file.getPath());

        writer.close();
    }
}
