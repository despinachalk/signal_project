package com.data_management;

import com.alerts.AlertInterface;
import com.alerts.ECGAlertFactory;
import java.util.List;

public class HeartRateStrategy implements AlertStrategy {
    private ECGAlertFactory factory = new ECGAlertFactory();

    @Override
    public AlertInterface checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("ECG")) {
                if (record.getMeasurementValue() > 150 || record.getMeasurementValue() < 50) {
                    return factory.createAlert(
                            Integer.toString(patient.getPatientId()),
                            "Abnormal Heart Rate detected: " + record.getMeasurementValue(),
                            record.getTimestamp()
                    );
                }
            }
        }
        return null;
    }
}