package com.data_management;

import com.alerts.Alert;
import java.util.ArrayList;
import java.util.List;

/**
 * Strategy to evaluate if a patient's blood oxygen saturation is dangerously low
 * or dropping rapidly within a 10-minute window.
 */
public class BloodSaturationStrategy implements AlertStrategy {

    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        List<PatientRecord> saturationRecords = new ArrayList<>();

        //Isolate saturation records and check for straight drops below 92%
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("BloodSaturation")) {
                saturationRecords.add(record);
                if (record.getMeasurementValue() < 92.0) {
                    return new Alert(
                            Integer.toString(patient.getPatientId()),
                            "Low Blood Oxygen Saturation: " + record.getMeasurementValue() + "%",
                            record.getTimestamp()
                    );
                }
            }
        }

        // Rapid Drop Alert ,Drop of 5% or more within 10 minutes
        long tenMinutesInMs = 600000;

        for (int i = 0; i < saturationRecords.size(); i++) {
            PatientRecord startRecord = saturationRecords.get(i);

            // Compare the start record against subsequent records
            for (int j = i + 1; j < saturationRecords.size(); j++) {
                PatientRecord futureRecord = saturationRecords.get(j);

                // If the time gap exceeds 10 minutes, stop checking against this startRecord
                if (futureRecord.getTimestamp() - startRecord.getTimestamp() > tenMinutesInMs) {
                    break;
                }

                // If within the 10-minute window, did the value drop by 5 or more?
                if (startRecord.getMeasurementValue() - futureRecord.getMeasurementValue() >= 5.0) {
                    return new Alert(
                            Integer.toString(patient.getPatientId()),
                            "Rapid Drop in Blood Oxygen Saturation detected",
                            futureRecord.getTimestamp()
                    );
                }
            }
        }

        return null;
    }
}