package com.affirm.loan.repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This repository combines Facility and Covenants information.
 * The assumption is one facility will only come from one partner bank.
 */
public class FacilityRepository {
    final Map<Integer, Facility> facilities = new HashMap<>();
    final SortedMap<BigDecimal, List<Facility>> cheapestFacilitiesGroup = new TreeMap<>();

    public FacilityRepository(File facilityFile, File covenantFile) throws IOException {

        CSVReader reader = new CSVReaderBuilder(new FileReader(facilityFile))
                .withSkipLines(1)
                .build();
        String[] line;
        Map<Integer, List<Integer>> bankToFacilities = new HashMap<>();
        while ((line = reader.readNext()) != null) {
            Facility facility = new Facility(
                    Integer.parseInt(line[2]),
                    Integer.parseInt(line[3]),
                    new BigDecimal(line[0]),
                    new BigDecimal(line[1])
            );
            facilities.put(facility.getId(), facility);

            cheapestFacilitiesGroup.putIfAbsent(facility.getInterestRate(), new ArrayList<>());
            cheapestFacilitiesGroup.get(facility.getInterestRate()).add(facility);

            bankToFacilities.putIfAbsent(facility.getBankId(), new ArrayList<>());
            bankToFacilities.get(facility.getBankId()).add(facility.getId());
        }
        reader.close();

        reader = new CSVReaderBuilder(new FileReader(covenantFile)).withSkipLines(1).build();
        while ((line = reader.readNext()) != null) {
             List<Integer> ids;
             if (line[0].isEmpty()) {
                 int bankId = Integer.parseInt(line[2]);
                 ids = bankToFacilities.get(bankId);
             } else {
                 ids = Collections.singletonList(Integer.parseInt(line[0]));
             }
             for (int id : ids) {
                 Facility facility = facilities.get(id);
                 if (!line[1].isEmpty()) {
                     facility.setMaxDefaultLikelihood(new BigDecimal(line[1]));
                 }
                 if (!line[3].isEmpty()) {
                     facility.addBannedState(line[3]);
                 }
             }
        }
        reader.close();

    }

    public Facility findCheapestWithMostAmount(BigDecimal amount,
                                               BigDecimal defaultLikelihood,
                                               String state) {
        for (BigDecimal interestRate : cheapestFacilitiesGroup.keySet()) {
            List<Facility> facilities = cheapestFacilitiesGroup.get(interestRate);
            facilities.sort((m, n) -> (n.getAmount().compareTo(m.getAmount())));
            for (Facility facility : facilities) {
                if (facility.getAmount().compareTo(amount) < 0) {
                    break;
                }
                if (facility.getMaxDefaultLikelihood().compareTo(defaultLikelihood) >= 0
                        && !facility.getBannedStates().contains(state)) {
                    return facility;
                }
            }
        }
        return null;
    }
}
