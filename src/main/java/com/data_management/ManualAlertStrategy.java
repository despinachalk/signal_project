package com.data_management;

import com.alerts.Alert;
import java.util.List;

/**
 * Strategy to check if a manual alert button was triggered by a patient or nurse.
 */
public class ManualAlertStrategy implements AlertStrategy {

    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);

        // Only care about the most recent manual alert status
        for (int i = records.size() - 1; i >= 0; i--) {
            PatientRecord record = records.get(i);

            if (record.getRecordType().equals("Alert")) {
                // Assuming 1.0 represents "triggered" and 0.0 represents "resolved"
                if (record.getMeasurementValue() == 1.0) {
                    return new Alert(
                            Integer.toString(patient.getPatientId()),
                            "Manual Button Triggered",
                            record.getTimestamp()
                    );
                } else if (record.getMeasurementValue() == 0.0) {
                    // The alert was resolved, so we don't trigger a new one
                    return null;
                }
            }
        }

        return null;
    }
}