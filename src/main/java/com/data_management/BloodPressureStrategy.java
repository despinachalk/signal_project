package com.data_management;

import com.alerts.AlertInterface;
import com.alerts.BloodPressureAlertFactory;
import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {
    private BloodPressureAlertFactory factory = new BloodPressureAlertFactory();

    @Override
    public AlertInterface checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("SystolicBloodPressure")) {
                if (record.getMeasurementValue() > 180 || record.getMeasurementValue() < 90) {
                    return factory.createAlert(
                            Integer.toString(patient.getPatientId()),
                            "Critical Blood Pressure: " + record.getMeasurementValue(),
                            record.getTimestamp()
                    );
                }
            }
        }
        return null;
    }
}