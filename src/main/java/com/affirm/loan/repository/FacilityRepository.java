package com.affirm.loan.repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    // This is needed when we add new facilities, to make sure the previous bank covenants will apply
    final Map<Integer, Covenants> bankOnlyCovenants = new HashMap<>();
    final Map<Integer, List<Facility>> bankToFacilities = new HashMap<>();

    public FacilityRepository(File facilityFile, File covenantFile) throws IOException {
        addFacilities(facilityFile);
        addCovenants(covenantFile);
    }

    public void addFacilities(File facilityFile) throws IOException {
        CSVReader reader = new CSVReaderBuilder(new FileReader(facilityFile))
                .withSkipLines(1)
                .build();
        String[] line;
        while ((line = reader.readNext()) != null) {
            Facility facility = new Facility(
                    Integer.parseInt(line[2]),
                    Integer.parseInt(line[3]),
                    new BigDecimal(line[0]),
                    new BigDecimal(line[1])
            );
            facilities.put(facility.getId(), facility);
            if (bankOnlyCovenants.containsKey(facility.getBankId())) {
                Covenants bankCovenants = bankOnlyCovenants.get(facility.getBankId());
                facility.getCovenants().add(bankCovenants);
            }

            cheapestFacilitiesGroup.putIfAbsent(facility.getInterestRate(), new ArrayList<>());
            cheapestFacilitiesGroup.get(facility.getInterestRate()).add(facility);

            bankToFacilities.putIfAbsent(facility.getBankId(), new ArrayList<>());
            bankToFacilities.get(facility.getBankId()).add(facility);
        }
        reader.close();
    }

    public void addCovenants(File covenantFile) throws IOException {
        CSVReader reader = new CSVReaderBuilder(new FileReader(covenantFile)).withSkipLines(1).build();
        String[] line;
        while ((line = reader.readNext()) != null) {
            if (line[0].isEmpty()) {
                int bankId = Integer.parseInt(line[2]);
                bankOnlyCovenants.putIfAbsent(bankId, new Covenants());
                Covenants covenants = bankOnlyCovenants.get(bankId);
                if (!line[1].isEmpty()) {
                    covenants.setMaxDefaultLikelihood(new BigDecimal(line[1]));
                }
                if (!line[3].isEmpty()) {
                    covenants.addBannedState(line[3]);
                }
                for (Facility facility : bankToFacilities.get(bankId)) {
                    facility.getCovenants().add(covenants);
                }
            } else {
                int id = Integer.parseInt(line[0]);
                Covenants covenants = facilities.get(id).getCovenants();
                if (!line[1].isEmpty()) {
                    covenants.setMaxDefaultLikelihood(new BigDecimal(line[1]));
                }
                if (!line[3].isEmpty()) {
                    covenants.addBannedState(line[3]);
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
                if (facility.getCovenants().isSatisfied(defaultLikelihood, state)) {
                    return facility;
                }
            }
        }
        return null;
    }
}
