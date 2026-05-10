package com.data_management;

import com.alerts.Alert;
import java.util.List;

/*
 * Strategy to evaluate if a patient is experiencing Hypotensive Hypoxemia.
 * Triggers when Systolic Blood Pressure < 90 AND Blood Saturation < 92%.
 */
public class HypotensiveHypoxemiaStrategy implements AlertStrategy {

    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);

        // Use Double.NaN to represent that we haven't read a value yet
        double lastSystolic = Double.NaN;
        double lastSaturation = Double.NaN;

        for (PatientRecord record : records) {
            String type = record.getRecordType();

            if (type.equals("SystolicBloodPressure")) {
                lastSystolic = record.getMeasurementValue();
            } else if (type.equals("BloodSaturation")) {
                lastSaturation = record.getMeasurementValue();
            }

            // If we have recorded both at least once, check the condition
            if (!Double.isNaN(lastSystolic) && !Double.isNaN(lastSaturation)) {
                if (lastSystolic < 90.0 && lastSaturation < 92.0) {
                    return new Alert(
                            Integer.toString(patient.getPatientId()),
                            "Hypotensive Hypoxemia Alert! Systolic: " + lastSystolic + ", Saturation: " + lastSaturation + "%",
                            record.getTimestamp()
                    );
                }
            }
        }

        return null;
    }
}