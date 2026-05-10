package com.data_management;

import java.util.List;
import com.alerts.Alert;

/**
 * Strategy to evaluate if a patient's blood pressure crosses critical thresholds.
 */
public class BloodPressureThresholdStrategy implements AlertStrategy {

    @Override
    public Alert checkAlert(Patient patient) {
        // We need all records to find the most recent ones.
        // A large time window is used to get all records.
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);

        for (PatientRecord record : records) {
            String type = record.getRecordType();
            double value = record.getMeasurementValue();

            if (type.equals("SystolicBloodPressure")) {
                if (value > 180.0 || value < 90.0) {
                    return new Alert(
                            Integer.toString(patient.getPatientId()),
                            "Critical Systolic Blood Pressure: " + value,
                            record.getTimestamp()
                    );
                }
            } else if (type.equals("DiastolicBloodPressure")) {
                if (value > 120.0 || value < 60.0) {
                    return new Alert(
                            Integer.toString(patient.getPatientId()),
                            "Critical Diastolic Blood Pressure: " + value,
                            record.getTimestamp()
                    );
                }
            }
        }

        // No critical thresholds crossed
        return null;
    }
}