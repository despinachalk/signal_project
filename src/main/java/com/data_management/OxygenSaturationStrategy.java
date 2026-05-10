package com.data_management;

import com.alerts.AlertInterface;
import com.alerts.BloodOxygenAlertFactory;
import java.util.List;

public class OxygenSaturationStrategy implements AlertStrategy {
    private BloodOxygenAlertFactory factory = new BloodOxygenAlertFactory();

    @Override
    public AlertInterface checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("BloodSaturation")) {
                if (record.getMeasurementValue() < 92.0) {
                    return factory.createAlert(
                            Integer.toString(patient.getPatientId()),
                            "Low Oxygen Saturation: " + record.getMeasurementValue() + "%",
                            record.getTimestamp()
                    );
                }
            }
        }
        return null;
    }
}